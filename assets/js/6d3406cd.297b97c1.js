"use strict";(self.webpackChunkimproved_factions=self.webpackChunkimproved_factions||[]).push([[5207],{7614:(e,s,r)=>{r.r(s),r.d(s,{assets:()=>a,contentTitle:()=>t,default:()=>h,frontMatter:()=>c,metadata:()=>n,toc:()=>o});const n=JSON.parse('{"id":"commands/base/claim","title":"Claim","description":"Module: Base","source":"@site/docs/commands/base/claim.md","sourceDirName":"commands/base","slug":"/commands/base/claim","permalink":"/ImprovedFactions/docs/commands/base/claim","draft":false,"unlisted":false,"editUrl":"https://github.com/ToberoCat/ImprovedFactions/tree/dev/improved-factions-docs/docs/commands/base/claim.md","tags":[{"inline":true,"label":"Base","permalink":"/ImprovedFactions/docs/tags/base"},{"inline":true,"label":"Claim commands","permalink":"/ImprovedFactions/docs/tags/claim-commands"},{"inline":true,"label":"Claim","permalink":"/ImprovedFactions/docs/tags/claim"}],"version":"current","frontMatter":{"keywords":["Base","Claim commands","Claim"],"tags":["Base","Claim commands","Claim"]},"sidebar":"sidebar","previous":{"title":"Ban","permalink":"/ImprovedFactions/docs/commands/base/ban"},"next":{"title":"Create","permalink":"/ImprovedFactions/docs/commands/base/create"}}');var i=r(4848),d=r(8453);const c={keywords:["Base","Claim commands","Claim"],tags:["Base","Claim commands","Claim"]},t="Claim",a={},o=[{value:"Description",id:"description",level:2},{value:"Usage",id:"usage",level:2},{value:"For Player \ud83d\udc64",id:"for-player-",level:3},{value:"Parameters",id:"parameters",level:2},{value:"For Player \ud83d\udc64",id:"for-player--1",level:3},{value:"Permissions",id:"permissions",level:2},{value:"Responses",id:"responses",level:2}];function l(e){const s={blockquote:"blockquote",code:"code",h1:"h1",h2:"h2",h3:"h3",header:"header",p:"p",pre:"pre",strong:"strong",table:"table",tbody:"tbody",td:"td",th:"th",thead:"thead",tr:"tr",...(0,d.R)(),...e.components},{Details:r}=s;return r||function(e,s){throw new Error("Expected "+(s?"component":"object")+" `"+e+"` to be defined: you likely forgot to import, pass, or provide it.")}("Details",!0),(0,i.jsxs)(i.Fragment,{children:[(0,i.jsx)(s.header,{children:(0,i.jsx)(s.h1,{id:"claim",children:"Claim"})}),"\n",(0,i.jsxs)(s.blockquote,{children:["\n",(0,i.jsx)(s.p,{children:"Module: Base"}),"\n"]}),"\n",(0,i.jsx)(s.h2,{id:"description",children:"Description"}),"\n",(0,i.jsx)(s.p,{children:"Claim a chunk"}),"\n",(0,i.jsx)(s.h2,{id:"usage",children:"Usage"}),"\n",(0,i.jsx)(s.h3,{id:"for-player-",children:"For Player \ud83d\udc64"}),"\n",(0,i.jsx)(s.pre,{children:(0,i.jsx)(s.code,{className:"language-bash",children:"/factions claim <radius>\n"})}),"\n",(0,i.jsx)(s.h2,{id:"parameters",children:"Parameters"}),"\n",(0,i.jsx)(s.h3,{id:"for-player--1",children:"For Player \ud83d\udc64"}),"\n",(0,i.jsxs)(r,{children:[(0,i.jsx)("summary",{children:"View Parameters"}),(0,i.jsxs)(s.table,{children:[(0,i.jsx)(s.thead,{children:(0,i.jsxs)(s.tr,{children:[(0,i.jsx)(s.th,{children:"Parameter"}),(0,i.jsx)(s.th,{children:"Type"}),(0,i.jsx)(s.th,{children:"Required"}),(0,i.jsx)(s.th,{children:"Description"})]})}),(0,i.jsx)(s.tbody,{children:(0,i.jsxs)(s.tr,{children:[(0,i.jsx)(s.td,{children:"radius"}),(0,i.jsx)(s.td,{children:"Int"}),(0,i.jsx)(s.td,{children:"No"}),(0,i.jsx)(s.td,{children:(0,i.jsx)(s.code,{children:"The radius you want to claim"})})]})})]})]}),"\n",(0,i.jsx)(s.h2,{id:"permissions",children:"Permissions"}),"\n",(0,i.jsxs)(s.p,{children:["\ud83d\udd12 ",(0,i.jsx)(s.strong,{children:"Permission Required:"})," ",(0,i.jsx)(s.code,{children:"factions.commands.claim"})]}),"\n",(0,i.jsx)(s.h2,{id:"responses",children:"Responses"}),"\n",(0,i.jsxs)(s.table,{children:[(0,i.jsx)(s.thead,{children:(0,i.jsxs)(s.tr,{children:[(0,i.jsx)(s.th,{children:"Response Code"}),(0,i.jsx)(s.th,{children:"Description"})]})}),(0,i.jsxs)(s.tbody,{children:[(0,i.jsxs)(s.tr,{children:[(0,i.jsx)(s.td,{children:(0,i.jsx)(s.code,{children:"claimed"})}),(0,i.jsx)(s.td,{children:(0,i.jsx)(s.code,{children:"{prefix} <green>You've successfully claimed the chunk</green>"})})]}),(0,i.jsxs)(s.tr,{children:[(0,i.jsx)(s.td,{children:(0,i.jsx)(s.code,{children:"claimedRadius"})}),(0,i.jsx)(s.td,{children:(0,i.jsx)(s.code,{children:"{prefix} <green>You've successfully claimed the chunks</green>"})})]}),(0,i.jsxs)(s.tr,{children:[(0,i.jsx)(s.td,{children:(0,i.jsx)(s.code,{children:"notInFaction"})}),(0,i.jsx)(s.td,{children:(0,i.jsx)(s.code,{children:"{prefix} <red>Error: You're not in a faction</red>"})})]}),(0,i.jsxs)(s.tr,{children:[(0,i.jsx)(s.td,{children:(0,i.jsx)(s.code,{children:"noPermission"})}),(0,i.jsx)(s.td,{children:(0,i.jsx)(s.code,{children:"{prefix} <red>Error: You don't have the required permission</red>"})})]}),(0,i.jsxs)(s.tr,{children:[(0,i.jsx)(s.td,{children:(0,i.jsx)(s.code,{children:"missingRequiredArgument"})}),(0,i.jsx)(s.td,{children:(0,i.jsx)(s.code,{children:"{prefix} <red>Error: Missing required argument</red>"})})]})]})]})]})}function h(e={}){const{wrapper:s}={...(0,d.R)(),...e.components};return s?(0,i.jsx)(s,{...e,children:(0,i.jsx)(l,{...e})}):l(e)}},8453:(e,s,r)=>{r.d(s,{R:()=>c,x:()=>t});var n=r(6540);const i={},d=n.createContext(i);function c(e){const s=n.useContext(d);return n.useMemo((function(){return"function"==typeof e?e(s):{...s,...e}}),[s,e])}function t(e){let s;return s=e.disableParentContext?"function"==typeof e.components?e.components(i):e.components||i:c(e.components),n.createElement(d.Provider,{value:s},e.children)}}}]);