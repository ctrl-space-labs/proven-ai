import { useEffect } from "react";
import { useRouter } from "next/router";
import {localStorageConstants} from "src/utils/generalConstants";

const useRedirectOr404ForHome = (organizationId) => {
  const router = useRouter();

  useEffect(() => {
    const storedOrgId =
      organizationId ||
      window.localStorage.getItem(localStorageConstants.selectedOrganizationId);


    const effectiveOrgId = organizationId || storedOrgId;


    if (!effectiveOrgId ) {
      router.push("/404");
    } else if (!organizationId) {
      router.push(
        `/provenAI/home/?organizationId=${effectiveOrgId}`
      );
    }
  }, [organizationId, router]);
};

export default useRedirectOr404ForHome;
