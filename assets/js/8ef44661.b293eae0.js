"use strict";(self.webpackChunkdocumentation=self.webpackChunkdocumentation||[]).push([[8061],{5367:(e,n,o)=>{o.r(n),o.d(n,{assets:()=>d,contentTitle:()=>c,default:()=>h,frontMatter:()=>s,metadata:()=>i,toc:()=>l});var t=o(4848),r=o(8453);const s={},c=void 0,i={id:"ProvenAI - SDK/ISCC",title:"ISCC",description:"ISCC Code Gneration",source:"@site/docs/2. ProvenAI - SDK/4-ISCC.md",sourceDirName:"2. ProvenAI - SDK",slug:"/ProvenAI - SDK/ISCC",permalink:"/proven-ai/docs/ProvenAI - SDK/ISCC",draft:!1,unlisted:!1,editUrl:"https://github.com/ctrl-space-labs/proven-ai/tree/main/provenai-documentation/docs/2. ProvenAI - SDK/4-ISCC.md",tags:[],version:"current",sidebarPosition:4,frontMatter:{},sidebar:"tutorialSidebar",previous:{title:"Blockchain",permalink:"/proven-ai/docs/ProvenAI - SDK/Blockchain"},next:{title:"SSI Issuer and Verifier",permalink:"/proven-ai/docs/ProvenAI - SDK/SSI"}},d={},l=[{value:"ISCC Code Gneration",id:"iscc-code-gneration",level:2},{value:"Components",id:"components",level:3},{value:"1. <strong>IsccCodeGenerator Interface</strong>",id:"1-iscccodegenerator-interface",level:4},{value:"2. IsccCodeGeneratorApi Class Documentation",id:"2-iscccodegeneratorapi-class-documentation",level:4},{value:"4. <strong>IsccCodeService Class</strong>",id:"4-iscccodeservice-class",level:4}];function a(e){const n={code:"code",h2:"h2",h3:"h3",h4:"h4",li:"li",p:"p",strong:"strong",ul:"ul",...(0,r.R)(),...e.components};return(0,t.jsxs)(t.Fragment,{children:[(0,t.jsx)(n.h2,{id:"iscc-code-gneration",children:"ISCC Code Gneration"}),"\n",(0,t.jsx)(n.h3,{id:"components",children:"Components"}),"\n",(0,t.jsxs)(n.h4,{id:"1-iscccodegenerator-interface",children:["1. ",(0,t.jsx)(n.strong,{children:"IsccCodeGenerator Interface"})]}),"\n",(0,t.jsxs)(n.ul,{children:["\n",(0,t.jsxs)(n.li,{children:[(0,t.jsx)(n.strong,{children:"Purpose"}),": Defines a method to generate an ISCC code for a document."]}),"\n",(0,t.jsxs)(n.li,{children:[(0,t.jsx)(n.strong,{children:"Method"}),":","\n",(0,t.jsxs)(n.ul,{children:["\n",(0,t.jsxs)(n.li,{children:[(0,t.jsx)(n.code,{children:"getDocumentUniqueIdentifier(byte[] fileBytes, String originalDocumentName)"}),": Generates an ISCC code for the provided document.","\n",(0,t.jsxs)(n.ul,{children:["\n",(0,t.jsxs)(n.li,{children:[(0,t.jsx)(n.strong,{children:"Parameters"}),":","\n",(0,t.jsxs)(n.ul,{children:["\n",(0,t.jsxs)(n.li,{children:[(0,t.jsx)(n.code,{children:"fileBytes"}),": The byte array of the document."]}),"\n",(0,t.jsxs)(n.li,{children:[(0,t.jsx)(n.code,{children:"originalDocumentName"}),": The name of the document."]}),"\n"]}),"\n"]}),"\n",(0,t.jsxs)(n.li,{children:[(0,t.jsx)(n.strong,{children:"Returns"}),": An instance of ",(0,t.jsx)(n.code,{children:"IsccCodeResponse"}),"."]}),"\n"]}),"\n"]}),"\n"]}),"\n"]}),"\n"]}),"\n",(0,t.jsx)(n.h4,{id:"2-iscccodegeneratorapi-class-documentation",children:"2. IsccCodeGeneratorApi Class Documentation"}),"\n",(0,t.jsxs)(n.p,{children:["The ",(0,t.jsx)(n.code,{children:"IsccCodeGeneratorApi"})," class is responsible for interacting with the ISCC code generation API. It manages API calls for generating ISCC codes based on uploaded documents. For more information about the ISCC code generation API and the response returned see"]}),"\n",(0,t.jsxs)(n.h4,{id:"4-iscccodeservice-class",children:["4. ",(0,t.jsx)(n.strong,{children:"IsccCodeService Class"})]}),"\n",(0,t.jsxs)(n.p,{children:["Implements the ",(0,t.jsx)(n.code,{children:"IsccCodeGenerator"})," interface to generate ISCC codes."]}),"\n",(0,t.jsxs)(n.ul,{children:["\n",(0,t.jsxs)(n.li,{children:[(0,t.jsx)(n.strong,{children:"Methods"}),":","\n",(0,t.jsxs)(n.ul,{children:["\n",(0,t.jsxs)(n.li,{children:[(0,t.jsx)(n.code,{children:"getDocumentIsccCode(MultipartFile file, String originalDocumentName)"}),": Retrieves the ISCC code for a MultipartFile."]}),"\n",(0,t.jsxs)(n.li,{children:[(0,t.jsx)(n.code,{children:"getDocumentUniqueIdentifier(byte[] fileBytes, String originalDocumentName)"}),": Calls the API to generate the ISCC code."]}),"\n"]}),"\n"]}),"\n"]})]})}function h(e={}){const{wrapper:n}={...(0,r.R)(),...e.components};return n?(0,t.jsx)(n,{...e,children:(0,t.jsx)(a,{...e})}):a(e)}},8453:(e,n,o)=>{o.d(n,{R:()=>c,x:()=>i});var t=o(6540);const r={},s=t.createContext(r);function c(e){const n=t.useContext(s);return t.useMemo((function(){return"function"==typeof e?e(n):{...n,...e}}),[n,e])}function i(e){let n;return n=e.disableParentContext?"function"==typeof e.components?e.components(r):e.components||r:c(e.components),t.createElement(s.Provider,{value:n},e.children)}}}]);