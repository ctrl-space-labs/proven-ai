"use strict";(self.webpackChunkdocumentation=self.webpackChunkdocumentation||[]).push([[8892],{5327:(e,n,t)=>{t.r(n),t.d(n,{assets:()=>a,contentTitle:()=>d,default:()=>h,frontMatter:()=>s,metadata:()=>l,toc:()=>o});var i=t(4848),r=t(8453);const s={},d=void 0,l={id:"API Documentation/Agents",title:"Agents",description:"Issue Agent Verifiable Credential",source:"@site/docs/3. API Documentation/4-Agents.md",sourceDirName:"3. API Documentation",slug:"/API Documentation/Agents",permalink:"/proven-ai/docs/API Documentation/Agents",draft:!1,unlisted:!1,editUrl:"https://github.com/ctrl-space-labs/proven-ai/tree/main/provenai-documentation/docs/3. API Documentation/4-Agents.md",tags:[],version:"current",sidebarPosition:4,frontMatter:{},sidebar:"tutorialSidebar",previous:{title:"Data Pods",permalink:"/proven-ai/docs/API Documentation/Data Pods"},next:{title:"Search",permalink:"/proven-ai/docs/API Documentation/Search"}},a={},o=[{value:"Issue Agent Verifiable Credential",id:"issue-agent-verifiable-credential",level:2},{value:"<strong>HTTP Method:</strong>",id:"http-method",level:3},{value:"<strong>URL:</strong>",id:"url",level:3},{value:"Prerequites",id:"prerequites",level:3},{value:"Path Variable",id:"path-variable",level:3},{value:"Response:",id:"response",level:3},{value:"Authorize Agent in ProvenAI",id:"authorize-agent-in-provenai",level:2},{value:"<strong>HTTP Method:</strong>",id:"http-method-1",level:3},{value:"<strong>URL:</strong>",id:"url-1",level:3},{value:"Prerequites",id:"prerequites-1",level:3},{value:"Request Parameters",id:"request-parameters",level:3},{value:"Response:",id:"response-1",level:3}];function c(e){const n={a:"a",code:"code",h2:"h2",h3:"h3",li:"li",p:"p",strong:"strong",table:"table",tbody:"tbody",td:"td",th:"th",thead:"thead",tr:"tr",ul:"ul",...(0,r.R)(),...e.components};return(0,i.jsxs)(i.Fragment,{children:[(0,i.jsx)(n.h2,{id:"issue-agent-verifiable-credential",children:"Issue Agent Verifiable Credential"}),"\n",(0,i.jsx)(n.p,{children:"Issue verifiable credential for an agent using the W3C standard. The API provides the signe agent Verifiable credential in JWT format. This is created from the provenAI SDK using the walt.id SDK for credential issuance. Also it gnerates an OID4VC offer URL that can be accepted by any OID compliant wallet to receive credential, using the walt.id Credential Issuance API from the provenAI SDK."}),"\n",(0,i.jsx)(n.h3,{id:"http-method",children:(0,i.jsx)(n.strong,{children:"HTTP Method:"})}),"\n",(0,i.jsx)(n.p,{children:(0,i.jsx)(n.code,{children:"POST"})}),"\n",(0,i.jsx)(n.h3,{id:"url",children:(0,i.jsx)(n.strong,{children:"URL:"})}),"\n",(0,i.jsx)(n.p,{children:(0,i.jsx)(n.code,{children:"/agents/{agentIdd}/credential-offer"})}),"\n",(0,i.jsxs)(n.ul,{children:["\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.strong,{children:"Summary:"})," Get the signed jwt format of the agent VC and its offer url."]}),"\n"]}),"\n",(0,i.jsx)(n.h3,{id:"prerequites",children:"Prerequites"}),"\n",(0,i.jsx)(n.p,{children:"Issuer did and key in JWK format. In this context, provenAI is the issuer. These values are passed as environment variables."}),"\n",(0,i.jsxs)(n.table,{children:[(0,i.jsx)(n.thead,{children:(0,i.jsxs)(n.tr,{children:[(0,i.jsx)(n.th,{children:"Environment Variable"}),(0,i.jsx)(n.th,{children:"Description"}),(0,i.jsx)(n.th,{children:"Example Value"})]})}),(0,i.jsxs)(n.tbody,{children:[(0,i.jsxs)(n.tr,{children:[(0,i.jsx)(n.td,{children:(0,i.jsx)(n.code,{children:"ISSUER_DID"})}),(0,i.jsx)(n.td,{children:"The Decentralized Identifier of the issuer"}),(0,i.jsx)(n.td,{children:(0,i.jsx)(n.code,{children:"did:example:123456789abcdefghi"})})]}),(0,i.jsxs)(n.tr,{children:[(0,i.jsx)(n.td,{children:(0,i.jsx)(n.code,{children:"ISSUER_PRIVATE_JWK"})}),(0,i.jsx)(n.td,{children:"The JSON Web Key of the issuer in JWK format"}),(0,i.jsx)(n.td,{children:(0,i.jsx)(n.code,{children:'{"kty":"EC","crv":"P-256","x":"...","y":"..."}'})})]})]})]}),"\n",(0,i.jsx)(n.h3,{id:"path-variable",children:"Path Variable"}),"\n",(0,i.jsxs)(n.ul,{children:["\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.code,{children:"id"}),": String agent ID."]}),"\n"]}),"\n",(0,i.jsx)(n.h3,{id:"response",children:"Response:"}),"\n",(0,i.jsxs)(n.ul,{children:["\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.code,{children:"AgentIdCredential"}),": Java class with fields:","\n",(0,i.jsxs)(n.ul,{children:["\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.code,{children:"agentId"}),": String agent ID."]}),"\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.code,{children:"credentialOfferUrl"}),": String credential offer url."]}),"\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.code,{children:"credentialJwt"}),": Object signed credential jwt."]}),"\n"]}),"\n"]}),"\n"]}),"\n",(0,i.jsx)(n.h2,{id:"authorize-agent-in-provenai",children:"Authorize Agent in ProvenAI"}),"\n",(0,i.jsx)(n.p,{children:"Authorizes an agent in the ProvenAI ecosystem, and returns an authorization token. The agent needs to provide a Verifiable Agent ID Presentation and once the presentation is verified, an authorization token is provided to the agent. To verify the valididty of the VP, the walt.id verifier SDK is used. The VP is chcked against the following policies:"}),"\n",(0,i.jsxs)(n.ul,{children:["\n",(0,i.jsx)(n.li,{children:"HolderBindingPolicy: ies that issuer of the Verifiable Presentation (presenter) is also the subject of all Verifiable Credentials contained within."}),"\n",(0,i.jsxs)(n.li,{children:["JwtSignaturePolicy",":Verifies"," the signature of the W3C JWT-VC"]}),"\n",(0,i.jsxs)(n.li,{children:["NotBeforeDatePolicy",":erifies"," that the credentials not-before date is correctly exceeded."]}),"\n",(0,i.jsxs)(n.li,{children:["ExpirationDatePolicy",":that"," the credentials expiration date (exp for JWTs) has not been exceeded.\nMore information about the supported policies provided ",(0,i.jsx)(n.a,{href:"https://docs.walt.id/verifier/api/credential-verification/policies",children:"here"}),"."]}),"\n"]}),"\n",(0,i.jsx)(n.h3,{id:"http-method-1",children:(0,i.jsx)(n.strong,{children:"HTTP Method:"})}),"\n",(0,i.jsx)(n.p,{children:(0,i.jsx)(n.code,{children:"POST"})}),"\n",(0,i.jsx)(n.h3,{id:"url-1",children:(0,i.jsx)(n.strong,{children:"URL:"})}),"\n",(0,i.jsx)(n.p,{children:(0,i.jsx)(n.code,{children:"/agents/token"})}),"\n",(0,i.jsxs)(n.ul,{children:["\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.strong,{children:"Summary:"})," Authorizes an agent and returns an authorization token."]}),"\n"]}),"\n",(0,i.jsx)(n.h3,{id:"prerequites-1",children:"Prerequites"}),"\n",(0,i.jsxs)(n.ul,{children:["\n",(0,i.jsx)(n.li,{children:"The signed Agent Verifiable Presentation ID JWT."}),"\n"]}),"\n",(0,i.jsx)(n.h3,{id:"request-parameters",children:"Request Parameters"}),"\n",(0,i.jsxs)(n.ul,{children:["\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.code,{children:"grant_type"}),": String grant type to be used. Must be 'vp_token'."]}),"\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.code,{children:"scope"}),": String scope of the request. Must be set to 'openid email'."]}),"\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.code,{children:"vp_token"}),": String signed agent VP ID in JWT format."]}),"\n"]}),"\n",(0,i.jsx)(n.h3,{id:"response-1",children:"Response:"}),"\n",(0,i.jsxs)(n.ul,{children:["\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.code,{children:"AccessTokenResponse"}),": Access token response from keycloak."]}),"\n"]}),"\n",(0,i.jsxs)(n.p,{children:["Also CRUD operations are available for the ",(0,i.jsx)(n.code,{children:"Agent"})," entity. Full API reference ",(0,i.jsx)(n.a,{href:"https://dev.proven-ai.ctrlspace.dev/proven-ai/api/v1/swagger-ui/index.html#/Agents",children:"here"}),"."]})]})}function h(e={}){const{wrapper:n}={...(0,r.R)(),...e.components};return n?(0,i.jsx)(n,{...e,children:(0,i.jsx)(c,{...e})}):c(e)}},8453:(e,n,t)=>{t.d(n,{R:()=>d,x:()=>l});var i=t(6540);const r={},s=i.createContext(r);function d(e){const n=i.useContext(s);return i.useMemo((function(){return"function"==typeof e?e(n):{...n,...e}}),[n,e])}function l(e){let n;return n=e.disableParentContext?"function"==typeof e.components?e.components(r):e.components||r:d(e.components),i.createElement(s.Provider,{value:n},e.children)}}}]);