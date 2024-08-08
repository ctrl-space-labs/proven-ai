package dev.ctrlspace.provenai.backend.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ctrlspace.provenai.backend.authentication.ProvenAIAuthenticationToken;
import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.authentication.OrganizationUserDTO;
import dev.ctrlspace.provenai.backend.model.authentication.UserProfile;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.AccessCriteria;
import dev.ctrlspace.provenai.backend.repositories.AclPoliciesRepository;
import dev.ctrlspace.provenai.backend.repositories.AgentPurposeOfUsePoliciesRepository;
import dev.ctrlspace.provenai.backend.repositories.AgentRepository;
import dev.ctrlspace.provenai.backend.repositories.DataPodRepository;
import dev.ctrlspace.provenai.backend.utils.constants.QueryParamNames;
import dev.ctrlspace.provenai.backend.utils.constants.UserNamesConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.codec.binary.Base32;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;
import  dev.ctrlspace.provenai.backend.repositories.OrganizationRepository;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@Component("securityUtils")
public class SecurityUtils {

    Logger logger = org.slf4j.LoggerFactory.getLogger(SecurityUtils.class);

    private ObjectMapper objectMapper;

    private JWTUtils jwtUtils;

    private AgentRepository agentRepository;

    private DataPodRepository dataPodRepository;

    private AclPoliciesRepository aclPoliciesRepository;

    private AgentPurposeOfUsePoliciesRepository agentPurposeOfUsePoliciesRepository;

    private OrganizationRepository organizationRepository;






    @Autowired
    public SecurityUtils(ObjectMapper objectMapper,
                         JWTUtils jwtUtils,
                         AgentRepository agentRepository,
                         DataPodRepository dataPodRepository,
                         AclPoliciesRepository aclPoliciesRepository,
                         AgentPurposeOfUsePoliciesRepository agentPurposeOfUsePoliciesRepository) {
        this.objectMapper = objectMapper;
        this.jwtUtils = jwtUtils;
        this.agentRepository = agentRepository;
        this.dataPodRepository = dataPodRepository;
        this.aclPoliciesRepository = aclPoliciesRepository;
        this.agentPurposeOfUsePoliciesRepository = agentPurposeOfUsePoliciesRepository;

    }



    public boolean isSuperAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return isSuperAdmin(authentication);

    }

    public boolean isSuperAdmin(Authentication authentication) {
        ProvenAIAuthenticationToken principal = (ProvenAIAuthenticationToken)SecurityContextHolder.getContext()
                .getAuthentication();
        return principal != null &&
                UserNamesConstants.GENDOX_SUPER_ADMIN.equals(
                        principal.getPrincipal().getGlobalUserRoleType().getName()
                );
    }

    public boolean isUser() {
        ProvenAIAuthenticationToken principal = (ProvenAIAuthenticationToken)SecurityContextHolder.getContext()
                .getAuthentication();
        return principal != null &&
                UserNamesConstants.GENDOX_USER.equals(
                        principal.getPrincipal().getGlobalUserRoleType().getName()
                );
    }




    public boolean can(String authority, ProvenAIAuthenticationToken authentication, AccessCriteria accessCriteria) {

        // Check if projectIds is not null and not empty, then check project access
        if (accessCriteria.getDataPodIds() != null && !accessCriteria.getDataPodIds().isEmpty()) {
            return canAccessDataPods(authority, authentication, accessCriteria.getDataPodIds());
        }

        // Check if orgIds is not null and not empty, then check organization access
        if (accessCriteria.getOrgIds() != null && !accessCriteria.getOrgIds().isEmpty()) {
            return canAccessOrganizations(authority, authentication, accessCriteria.getOrgIds());
        }

        if (accessCriteria.getAgentIds() != null) {
            return canAccessAgent(authority, authentication, accessCriteria.getAgentIds());
        }




        return false;
    }

    private static boolean canAccessDataPods(String authority, ProvenAIAuthenticationToken authentication, Set<String> requestedDataPodIds) {
        Set<String> authorizedDataPodIds = authentication
                .getPrincipal()
                .getOrganizations()
                .stream()
                .filter(org -> org.getAuthorities().contains(authority))
                .flatMap(org -> org.getProjects().stream())
                .filter(project -> requestedDataPodIds.contains(project.getId()))
                .map(proj -> proj.getId())
                .collect(Collectors.toSet());

        if (!authorizedDataPodIds.containsAll(requestedDataPodIds)) {
            return false;
        }

        return true;
    }




    private static boolean canAccessOrganizations(String authority, ProvenAIAuthenticationToken authentication, Set<String> requestedOrgIds) {
        Set<String> authorizedOrgIds = authentication
                .getPrincipal()
                .getOrganizations()
                .stream()
                .filter(org -> requestedOrgIds.contains(org.getId()))
                .filter(org -> org.getAuthorities().contains(authority))
                .map(OrganizationUserDTO::getId)
                .collect(Collectors.toSet());

        if (!authorizedOrgIds.containsAll(requestedOrgIds)) {
            return false;
        }

        return true;
    }

    private boolean canAccessAgent(String authority, ProvenAIAuthenticationToken authentication, Set<String> agentIds) {
        List<String> authorizedOrgIds = authentication
                .getPrincipal()
                .getOrganizations()
                .stream()
                .filter(org -> org.getAuthorities().contains(authority))
                .map(OrganizationUserDTO::getId)
                .collect(Collectors.toList());

        for (String agentId : agentIds) {
            if (agentRepository.existsByIdAndOrganizationIdIn(UUID.fromString(agentId), authorizedOrgIds)) {
                return true;
            }
        }

        return false;
    }


    private AccessCriteria getRequestedOrgsFromRequestParams() {
        HttpServletRequest request = getCurrentHttpRequest();
        //get request param with name "projectId"
        String organizationId = request.getParameter(QueryParamNames.ORGANIZATION_ID);
        String[] orgStrings = request.getParameterValues(QueryParamNames.ORGANIZATION_ID_IN);
        // 'splitProjectStrings' now contains individual elements, split by commas
        // You can now use 'splitProjectStrings' as required
        if (orgStrings != null) {
            orgStrings = Arrays.stream(orgStrings)
                    .flatMap(s -> Arrays.stream(s.split(",")))
                    .toArray(String[]::new);

        }

        if (organizationId == null && orgStrings == null) {
            return new AccessCriteria();
        }

        Set<String> requestedOrgIds = new HashSet<>();
        if (orgStrings != null) {
            requestedOrgIds.addAll(Set.of(orgStrings));
        }
        if (organizationId != null) {
            requestedOrgIds.add(organizationId);
        }
        return AccessCriteria
                .builder()
                .orgIds(requestedOrgIds)
                .dataPodIds(new HashSet<>())
                .build();
    }





    @Nullable
    private AccessCriteria getRequestedOrgIdFromPathVariable() {
        // Extract organizationId from the request path
        HttpServletRequest request = getCurrentHttpRequest();
        Map<String, String> uriTemplateVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        String organizationId = new String();

        if (uriTemplateVariables != null) {
            organizationId = uriTemplateVariables.get(QueryParamNames.ORGANIZATION_ID);
        }
        Set<String> requestedOrgIds = new HashSet<>();

        if (organizationId != null) {
            requestedOrgIds.add(organizationId);
        }

        return AccessCriteria
                .builder()
                .orgIds(requestedOrgIds)
                .dataPodIds(new HashSet<>())
                .build();
    }


    private AccessCriteria getRequestedDataPodsFromRequestParams() {
        HttpServletRequest request = getCurrentHttpRequest();
        //get request param with name "projectId"
        String dataPodId = request.getParameter(QueryParamNames.DATAPOD_ID);
        String[] dataPodStrings = request.getParameterValues(QueryParamNames.DATAPOD_ID_IN);
        // 'splitProjectStrings' now contains individual elements, split by commas
        // You can now use 'splitProjectStrings' as required
        if (dataPodStrings != null) {
            dataPodStrings = Arrays.stream(dataPodStrings)
                    .flatMap(s -> Arrays.stream(s.split(",")))
                    .toArray(String[]::new);


        }

        if (dataPodId == null && dataPodStrings == null) {
            return new AccessCriteria();
        }

        Set<String> requestedDataPodIds = new HashSet<>();
        if (dataPodStrings != null) {
            requestedDataPodIds.addAll(Set.of(dataPodStrings));
        }
        if (dataPodId != null) {
            requestedDataPodIds.add(dataPodId);
        }
        return AccessCriteria
                .builder()
                .orgIds(new HashSet<>())
                .dataPodIds(requestedDataPodIds)
                .build();
    }

    private AccessCriteria getRequestedAgentsFromRequestParams() {
        HttpServletRequest request = getCurrentHttpRequest();
        //get request param with name "projectId"
        String agentId = request.getParameter(QueryParamNames.AGENT_ID);
        String[] agentStrings = request.getParameterValues(QueryParamNames.AGENT_ID_IN);
        // 'splitProjectStrings' now contains individual elements, split by commas
        // You can now use 'splitProjectStrings' as required
        if (agentStrings != null) {
            agentStrings = Arrays.stream(agentStrings)
                    .flatMap(s -> Arrays.stream(s.split(",")))
                    .toArray(String[]::new);


        }

        if (agentId == null && agentStrings == null) {
            new AccessCriteria();
        }

        Set<String> requestedAgentIds = new HashSet<>();
        if (agentStrings != null) {
            requestedAgentIds.addAll(Set.of(agentStrings));
        }
        if (agentId != null) {
            requestedAgentIds.add(agentId);
        }
        return AccessCriteria
                .builder()
                .orgIds(new HashSet<>())
                .dataPodIds(new HashSet<>())
                .agentIds(requestedAgentIds)
                .build();
    }

    @Nullable
    private AccessCriteria getRequestedDataPodIdFromPathVariable() {
        // Extract organizationId from the request path
        HttpServletRequest request = getCurrentHttpRequest();
        Map<String, String> uriTemplateVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        String dataPodId = new String();
        if (uriTemplateVariables != null) {
            dataPodId = uriTemplateVariables.get(QueryParamNames.DATAPOD_ID);
        }
        Set<String> requestedDataPodIds = new HashSet<>();

        if (dataPodId != null) {
            requestedDataPodIds.add(dataPodId);
        }

        return AccessCriteria
                .builder()
                .orgIds(new HashSet<>())
                .dataPodIds(requestedDataPodIds)
                .build();
    }

    private AccessCriteria getRequestedAgentIdFromPathVariable() {
        // Extract threadId from the request path
        HttpServletRequest request = getCurrentHttpRequest();
        Map<String, String> uriTemplateVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        String agentId = new String();

        if (uriTemplateVariables != null) {
            agentId = uriTemplateVariables.get(QueryParamNames.AGENT_ID);
        }

        Set<String> requestedAgentIds = new HashSet<>();

        if (agentId != null) {
            requestedAgentIds.add(agentId);
        }


        return AccessCriteria
                .builder()
                .orgIds(new HashSet<>())
                .dataPodIds(new HashSet<>())
                .agentIds(requestedAgentIds)
                .build();
    }








    public class AccessCriteriaGetterFunction {

        public static final String ORG_IDS_FROM_REQUEST_PARAMS = "getRequestedOrgsFromRequestParams";
        public static final String ORG_ID_FROM_PATH_VARIABLE = "getRequestedOrgIdFromPathVariable";

        public static final String DATAPOD_IDS_FROM_REQUEST_PARAMS = "getRequestedDataPodsFromRequestParams";
        public static final String DATAPOD_ID_FROM_PATH_VARIABLE = "getRequestedDataPodIdFromPathVariable";

        public static final String AGENT_ID_FROM_PATH_VARIABLE = "getRequestedAgentIdFromPathVariable";

        public static final String AGENT_IDS_FROM_REQUEST_PARAMS = "getRequestedAgentsFromRequestParams";



    }


    /**
     * This is a general method to check for Authorization
     *
     * @param authority      the authority that the user should have
     * @param getterFunction this is used to find the appropriate function, that will extract the {@link AccessCriteria}
     *                       from path variables or requestparams or JSON body
     * @return
     */
    public boolean hasAuthority(String authority, String getterFunction) throws IOException {
        ProvenAIAuthenticationToken authentication = (ProvenAIAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        boolean fromRequestParams = false;
        AccessCriteria accessCriteria = new AccessCriteria();


        if (AccessCriteriaGetterFunction.ORG_IDS_FROM_REQUEST_PARAMS.equals(getterFunction)) {
            accessCriteria = getRequestedOrgsFromRequestParams();
            fromRequestParams = true;
        }
        if (AccessCriteriaGetterFunction.ORG_ID_FROM_PATH_VARIABLE.equals(getterFunction)) {
            accessCriteria = getRequestedOrgIdFromPathVariable();
        }


        if (AccessCriteriaGetterFunction.DATAPOD_IDS_FROM_REQUEST_PARAMS.equals(getterFunction)) {
            accessCriteria = getRequestedDataPodsFromRequestParams();
            fromRequestParams = true;
        }
        if (AccessCriteriaGetterFunction.DATAPOD_ID_FROM_PATH_VARIABLE.equals(getterFunction)) {
            accessCriteria = getRequestedDataPodIdFromPathVariable();
        }

        if (AccessCriteriaGetterFunction.AGENT_ID_FROM_PATH_VARIABLE.equals(getterFunction)) {
            accessCriteria = getRequestedAgentIdFromPathVariable();

        }

        if (AccessCriteriaGetterFunction.AGENT_IDS_FROM_REQUEST_PARAMS.equals(getterFunction)) {
            accessCriteria = getRequestedAgentsFromRequestParams();
            fromRequestParams = true;
        }


        if (accessCriteria == null|| accessCriteria.isEmpty()) {
            return fromRequestParams;
        }
        return can(authority, authentication, accessCriteria);
    }


    public UUID getUserId() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = ((UserProfile) authentication.getPrincipal()).getId();
            return UUID.fromString(userId);
        } catch (Exception e) {
            logger.warn("An exception occurred while trying to get the user ID: " + e.getMessage());
            return null;
        }
    }

    public String getUserIdentifier() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = ((UserProfile) authentication.getPrincipal()).getEmail();
            if (email != null) {
                return email;
            }
            return ((UserProfile) authentication.getPrincipal()).getUserName();
        } catch (Exception e){
            logger.warn("An exception occurred while trying to get the user ID: " + e.getMessage());
            return null;
        }
    }

    private HttpServletRequest getCurrentHttpRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) requestAttributes).getRequest();
        }

        return null;
    }


    public static String calculateSHA256(String text) throws ProvenAiException {
        try {
            // Get an instance of SHA-256 MessageDigest
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // Calculate the hash
            byte[] hashBytes = digest.digest(text.getBytes());
            // Encode the hash in Base32
            Base32 base32 = new Base32();
            return base32.encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new ProvenAiException("HASHING_ERROR", "An error occurred while hashing the text", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}