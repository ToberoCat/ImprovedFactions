"use strict";(self.webpackChunkimproved_factions=self.webpackChunkimproved_factions||[]).push([[336],{846:(e,n,s)=>{s.r(n),s.d(n,{assets:()=>o,contentTitle:()=>t,default:()=>h,frontMatter:()=>c,metadata:()=>r,toc:()=>a});const r=JSON.parse('{"id":"commands/base/unclaim","title":"Unclaim","description":"Module: Base","source":"@site/docs/commands/base/unclaim.md","sourceDirName":"commands/base","slug":"/commands/base/unclaim","permalink":"/ImprovedFactions/docs/commands/base/unclaim","draft":false,"unlisted":false,"editUrl":"https://github.com/ToberoCat/ImprovedFactions/tree/dev/improved-factions-docs/docs/commands/base/unclaim.md","tags":[{"inline":true,"label":"Base","permalink":"/ImprovedFactions/docs/tags/base"},{"inline":true,"label":"Claim commands","permalink":"/ImprovedFactions/docs/tags/claim-commands"},{"inline":true,"label":"Unclaim","permalink":"/ImprovedFactions/docs/tags/unclaim"}],"version":"current","frontMatter":{"keywords":["Base","Claim commands","Unclaim"],"tags":["Base","Claim commands","Unclaim"]},"sidebar":"sidebar","previous":{"title":"Unban","permalink":"/ImprovedFactions/docs/commands/base/unban"},"next":{"title":"Version","permalink":"/ImprovedFactions/docs/commands/base/version"}}');var i=s(4848),d=s(8453);const c={keywords:["Base","Claim commands","Unclaim"],tags:["Base","Claim commands","Unclaim"]},t="Unclaim",o={},a=[{value:"Description",id:"description",level:2},{value:"Usage",id:"usage",level:2},{value:"For Player \ud83d\udc64",id:"for-player-",level:3},{value:"Parameters",id:"parameters",level:2},{value:"For Player \ud83d\udc64",id:"for-player--1",level:3},{value:"Permissions",id:"permissions",level:2},{value:"Responses",id:"responses",level:2}];function l(e){const n={blockquote:"blockquote",code:"code",h1:"h1",h2:"h2",h3:"h3",header:"header",p:"p",pre:"pre",strong:"strong",table:"table",tbody:"tbody",td:"td",th:"th",thead:"thead",tr:"tr",...(0,d.R)(),...e.components},{Details:s}=n;return s||function(e,n){throw new Error("Expected "+(n?"component":"object")+" `"+e+"` to be defined: you likely forgot to import, pass, or provide it.")}("Details",!0),(0,i.jsxs)(i.Fragment,{children:[(0,i.jsx)(n.header,{children:(0,i.jsx)(n.h1,{id:"unclaim",children:"Unclaim"})}),"\n",(0,i.jsxs)(n.blockquote,{children:["\n",(0,i.jsx)(n.p,{children:"Module: Base"}),"\n"]}),"\n",(0,i.jsx)(n.h2,{id:"description",children:"Description"}),"\n",(0,i.jsx)(n.p,{children:"Unclaim a chunk"}),"\n",(0,i.jsx)(n.h2,{id:"usage",children:"Usage"}),"\n",(0,i.jsx)(n.h3,{id:"for-player-",children:"For Player \ud83d\udc64"}),"\n",(0,i.jsx)(n.pre,{children:(0,i.jsx)(n.code,{className:"language-bash",children:"/factions unclaim <radius>\n"})}),"\n",(0,i.jsx)(n.h2,{id:"parameters",children:"Parameters"}),"\n",(0,i.jsx)(n.h3,{id:"for-player--1",children:"For Player \ud83d\udc64"}),"\n",(0,i.jsxs)(s,{children:[(0,i.jsx)("summary",{children:"View Parameters"}),(0,i.jsxs)(n.table,{children:[(0,i.jsx)(n.thead,{children:(0,i.jsxs)(n.tr,{children:[(0,i.jsx)(n.th,{children:"Parameter"}),(0,i.jsx)(n.th,{children:"Type"}),(0,i.jsx)(n.th,{children:"Required"}),(0,i.jsx)(n.th,{children:"Description"})]})}),(0,i.jsx)(n.tbody,{children:(0,i.jsxs)(n.tr,{children:[(0,i.jsx)(n.td,{children:"radius"}),(0,i.jsx)(n.td,{children:"Int"}),(0,i.jsx)(n.td,{children:"No"}),(0,i.jsx)(n.td,{children:(0,i.jsx)(n.code,{children:"The radius you want to unclaim"})})]})})]})]}),"\n",(0,i.jsx)(n.h2,{id:"permissions",children:"Permissions"}),"\n",(0,i.jsxs)(n.p,{children:["\ud83d\udd12 ",(0,i.jsx)(n.strong,{children:"Permission Required:"})," ",(0,i.jsx)(n.code,{children:"factions.commands.unclaim"})]}),"\n",(0,i.jsx)(n.h2,{id:"responses",children:"Responses"}),"\n",(0,i.jsxs)(n.table,{children:[(0,i.jsx)(n.thead,{children:(0,i.jsxs)(n.tr,{children:[(0,i.jsx)(n.th,{children:"Response Code"}),(0,i.jsx)(n.th,{children:"Description"})]})}),(0,i.jsxs)(n.tbody,{children:[(0,i.jsxs)(n.tr,{children:[(0,i.jsx)(n.td,{children:(0,i.jsx)(n.code,{children:"unclaimed"})}),(0,i.jsx)(n.td,{children:(0,i.jsx)(n.code,{children:"{prefix} <green>You've successfully unclaimed the chunk</green>"})})]}),(0,i.jsxs)(n.tr,{children:[(0,i.jsx)(n.td,{children:(0,i.jsx)(n.code,{children:"unclaimedRadius"})}),(0,i.jsx)(n.td,{children:(0,i.jsx)(n.code,{children:"{prefix} <green>You've successfully unclaimed the chunks</green>"})})]}),(0,i.jsxs)(n.tr,{children:[(0,i.jsx)(n.td,{children:(0,i.jsx)(n.code,{children:"notInFaction"})}),(0,i.jsx)(n.td,{children:(0,i.jsx)(n.code,{children:"{prefix} <red>Error: You're not in a faction</red>"})})]}),(0,i.jsxs)(n.tr,{children:[(0,i.jsx)(n.td,{children:(0,i.jsx)(n.code,{children:"noPermission"})}),(0,i.jsx)(n.td,{children:(0,i.jsx)(n.code,{children:"{prefix} <red>Error: You don't have the required permission</red>"})})]}),(0,i.jsxs)(n.tr,{children:[(0,i.jsx)(n.td,{children:(0,i.jsx)(n.code,{children:"missingRequiredArgument"})}),(0,i.jsx)(n.td,{children:(0,i.jsx)(n.code,{children:"{prefix} <red>Error: Missing required argument</red>"})})]})]})]})]})}function h(e={}){const{wrapper:n}={...(0,d.R)(),...e.components};return n?(0,i.jsx)(n,{...e,children:(0,i.jsx)(l,{...e})}):l(e)}},8453:(e,n,s)=>{s.d(n,{R:()=>c,x:()=>t});var r=s(6540);const i={},d=r.createContext(i);function c(e){const n=r.useContext(d);return r.useMemo((function(){return"function"==typeof e?e(n):{...n,...e}}),[n,e])}function t(e){let n;return n=e.disableParentContext?"function"==typeof e.components?e.components(i):e.components||i:c(e.components),r.createElement(d.Provider,{value:n},e.children)}}}]);