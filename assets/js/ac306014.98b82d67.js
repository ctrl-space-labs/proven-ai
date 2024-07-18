"use strict";(self.webpackChunkdocumentation=self.webpackChunkdocumentation||[]).push([[3079],{3959:(e,n,i)=>{i.r(n),i.d(n,{assets:()=>o,contentTitle:()=>d,default:()=>u,frontMatter:()=>t,metadata:()=>c,toc:()=>a});var r=i(4848),s=i(8453);const t={},d=void 0,c={id:"SSI Issuer & Verifier/4.1.Issuer",title:"4.1.Issuer",description:"4.1. ProvenAI Issuer",source:"@site/docs/4. SSI Issuer & Verifier/4.1.Issuer.md",sourceDirName:"4. SSI Issuer & Verifier",slug:"/SSI Issuer & Verifier/4.1.Issuer",permalink:"/docs/SSI Issuer & Verifier/4.1.Issuer",draft:!1,unlisted:!1,editUrl:"https://github.com/ctrl-space-labs/proven-ai/tree/feature-13-proven-ai-smart-contracts/provenai-documentation/docs/4. SSI Issuer & Verifier/4.1.Issuer.md",tags:[],version:"current",frontMatter:{},sidebar:"tutorialSidebar",previous:{title:"4.1 Introduction",permalink:"/docs/SSI Issuer & Verifier/Setup -Issuer-and-Verifiers"},next:{title:"5.1 Introduction",permalink:"/docs/ISCC Code/5.1 Introduction"}},o={},a=[{value:"4.1. ProvenAI Issuer",id:"41-provenai-issuer",level:2},{value:"Generate unsigned Verifiable Credentials Method",id:"generate-unsigned-verifiable-credentials-method",level:4},{value:"Generate signed Verifiable Credentials Method",id:"generate-signed-verifiable-credentials-method",level:4}];function l(e){const n={code:"code",h2:"h2",h4:"h4",li:"li",p:"p",strong:"strong",ul:"ul",...(0,s.R)(),...e.components};return(0,r.jsxs)(r.Fragment,{children:[(0,r.jsx)(n.h2,{id:"41-provenai-issuer",children:"4.1. ProvenAI Issuer"}),"\n",(0,r.jsxs)(n.p,{children:["The ",(0,r.jsx)(n.code,{children:"ProvenAIIssuer"})," class is designed to generate and sign Verifiable Credentials (VCs) for ProvenAI entities. It provides methods to convert VCs to the W3CVC standard format and to sign these credentials using the issuer's key.\nSupported Entities:"]}),"\n",(0,r.jsxs)(n.ul,{children:["\n",(0,r.jsx)(n.li,{children:"AI Agent"}),"\n",(0,r.jsx)(n.li,{children:"Permission of Use"}),"\n"]}),"\n",(0,r.jsx)(n.h4,{id:"generate-unsigned-verifiable-credentials-method",children:"Generate unsigned Verifiable Credentials Method"}),"\n",(0,r.jsx)(n.p,{children:"Generates a W3CVC credential based on a credential subject of a specific schema. It converts the specific credential subjects to adhere to the W3CVC standard in order to produce valid verifiable credentials."}),"\n",(0,r.jsxs)(n.p,{children:[(0,r.jsx)(n.strong,{children:"Signature:"})," ",(0,r.jsx)(n.code,{children:"generateUnsignedVC(VerifiableCredential&lt;? extends CredentialSubject&gt; vc)"}),"\n",(0,r.jsx)(n.strong,{children:"Parameters:"})," A verifiable credential subject object ",(0,r.jsx)(n.code,{children:"VerifiableCredential&lt;? extends CredentialSubject&gt; vc"}),".\n",(0,r.jsx)(n.strong,{children:"Return Type:"})," A W3CVC verifiable credential with the information provided from the credential subject.\n",(0,r.jsx)(n.strong,{children:"Exceptions"}),": JSONException, JsonProcessingException"]}),"\n",(0,r.jsx)(n.h4,{id:"generate-signed-verifiable-credentials-method",children:"Generate signed Verifiable Credentials Method"}),"\n",(0,r.jsxs)(n.p,{children:["Generates a signed W3CVC credential object in JWT format. It signs the VC using the walt.id issuer SDK method ",(0,r.jsx)(n.code,{children:"signJws"})]}),"\n",(0,r.jsxs)(n.p,{children:[(0,r.jsx)(n.strong,{children:"Signature:"})," ",(0,r.jsx)(n.code,{children:"generateSignedVCJwt(W3CVC w3cVC, Key issuerKey, String issuerDid, String subjectDid)"}),"\n",(0,r.jsx)(n.strong,{children:"Parameters:"})]}),"\n",(0,r.jsxs)(n.ul,{children:["\n",(0,r.jsxs)(n.li,{children:[(0,r.jsx)(n.code,{children:"w3cVC"}),": W3CVC type verifiable credential."]}),"\n",(0,r.jsxs)(n.li,{children:[(0,r.jsx)(n.code,{children:"issuerKey"}),": Key walt.id type. The issuer is the entity that signs the verifiable credential."]}),"\n",(0,r.jsxs)(n.li,{children:[(0,r.jsx)(n.code,{children:"issuerDid"}),": String issuer DID."]}),"\n",(0,r.jsxs)(n.li,{children:[(0,r.jsx)(n.code,{children:"subjectDid"}),": String subject DID. The subject is the holder of the verifiable credential.\n",(0,r.jsx)(n.strong,{children:"Return Type:"})," Object signed verifiable credential in JWT format."]}),"\n"]})]})}function u(e={}){const{wrapper:n}={...(0,s.R)(),...e.components};return n?(0,r.jsx)(n,{...e,children:(0,r.jsx)(l,{...e})}):l(e)}},8453:(e,n,i)=>{i.d(n,{R:()=>d,x:()=>c});var r=i(6540);const s={},t=r.createContext(s);function d(e){const n=r.useContext(t);return r.useMemo((function(){return"function"==typeof e?e(n):{...n,...e}}),[n,e])}function c(e){let n;return n=e.disableParentContext?"function"==typeof e.components?e.components(s):e.components||s:d(e.components),r.createElement(t.Provider,{value:n},e.children)}}}]);