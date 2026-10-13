const imgRectangle = "../assets/d23ee4c5-1d8b-41d3-98ad-18fee91515ca.svg";
const imgCombinedShape = "../assets/fc58472c-af0d-46e6-abc6-1d4ab534a25c.svg";
const imgWiFi = "../assets/c0ca72fb-83b9-4db0-8816-5ae8047c0237.svg";
const imgFrame32 = "../assets/617edea3-4fbc-4415-95ed-349807bd746f.png";
const imgFrame47 = "../assets/d49b49b6-c380-4c20-a8b0-624ac35ad1b5.png";
const imgFrame50 = "../assets/c31d7f36-3c5c-472b-8b11-86f388ae30f9.png";
const imgFrame54 = "../assets/44a2aecd-4679-49eb-ad49-e6b857f47494.png";
const imgFrame55 = "../assets/a1916367-5a3b-4324-aa24-e1346e9e48a2.png";
const imgFrame51 = "../assets/9db723c2-d550-4242-b8d2-1e93f6965403.png";
const imgFrame52 = "../assets/13766ca6-33c5-49cb-a1be-7502a8ca54d5.png";
const imgFrame53 = "../assets/0d23da9d-e246-41f4-a03a-3ac9aa4afbdc.png";
const imgFrame48 = "../assets/2c1f745a-8f63-41ba-a310-d3f9d0fc6ee5.png";
const imgFrame49 = "../assets/7429ac9a-dbac-4780-92ff-e6582f05e7ef.png";
const imgGroup5 = "../assets/0dfa833c-7721-4951-9827-4aa6abe583aa.svg";
const imgIconSearch = "../assets/b1fc9aa9-b6f4-4fcc-bc3c-5c0fd2489f42.svg";
const imgIconShoppingCart = "../assets/59ecd472-0634-4e9f-af95-546ba4e72402.svg";
const imgEllipse42 = "../assets/c423e36b-efec-4c9c-ab4a-77ef31d5ff38.svg";
const imgHeart33 = "../assets/ffcf0620-9ee9-4595-85df-fd05748f3e7c.svg";
const imgCategory13 = "../assets/5d9287a8-51a4-4fc5-828f-4f7af78cb61c.svg";
const imgHome13 = "../assets/7b03b532-3ed9-4a41-ba7d-a03f170125dd.svg";
const imgUser13 = "../assets/1a01c726-0361-4b46-8a51-6eaf1776c4ac.svg";

function StatusBar({ className }: { className?: string }) {
  return (
    <div className={className || "bg-white h-[44px] relative rounded-[20px] w-[428px]"} data-node-id="17:156" data-name="Status Bar">
      <div className="-translate-y-1/2 absolute contents left-[19.89px] top-[calc(50%+1px)]" data-node-id="17:157" data-name="Action">
        <p className="-translate-x-1/2 [word-break:break-word] absolute font-['SF_Pro_Text:Semibold'] leading-[18px] left-[46.89px] not-italic text-[15px] text-black text-center top-[calc(50%-8px)] tracking-[-0.165px] w-[54px]" data-node-id="17:158">
          9:41
        </p>
      </div>
      <div className="-translate-y-1/2 absolute contents right-[16px] top-[calc(50%-0.25px)]" data-node-id="17:159" data-name="Container">
        <div className="-translate-y-1/2 absolute contents right-[16px] top-[calc(50%-0.25px)]" data-node-id="17:160" data-name="Battery">
          <div className="-translate-y-1/2 absolute h-[11.5px] right-[16px] top-[calc(50%-0.25px)] w-[24.5px]" data-node-id="17:161" data-name="Rectangle">
            <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgRectangle} />
          </div>
          <div className="-translate-y-1/2 absolute bg-black h-[7.667px] right-[20.5px] rounded-[1.6px] top-[calc(50%-0.25px)] w-[18px]" data-node-id="17:166" data-name="Rectangle" />
        </div>
        <div className="-translate-y-1/2 absolute h-[10.7px] right-[65.9px] top-[calc(50%-0.21px)] w-[17.1px]" data-node-id="17:167" data-name="Combined Shape">
          <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgCombinedShape} />
        </div>
        <div className="-translate-y-1/2 absolute h-[11.057px] right-[45.5px] top-[calc(50%-0.23px)] w-[15.4px]" data-node-id="17:172" data-name="Wi-Fi">
          <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgWiFi} />
        </div>
      </div>
    </div>
  );
}

export default function CategoriesMen() {
  return (
    <div className="bg-white overflow-clip relative rounded-[20px] size-full" data-node-id="601:775" data-name="Categories-men">
      <StatusBar className="absolute bg-white h-[44px] left-px right-px rounded-[20px] top-0" />
      <div className="absolute h-[22px] left-[16px] top-[50px] w-[66px]" data-node-id="601:777">
        <div className="absolute inset-[-3.41%_-1.14%_-5.64%_-1.14%]">
          <img alt="" className="block max-w-none size-full" src={imgGroup5} />
        </div>
      </div>
      <div className="absolute bg-[rgba(255,255,255,0.7)] border border-[#004182] border-solid h-[50px] left-[16px] overflow-clip rounded-[25px] top-[91px] w-[348px]" data-node-id="601:791">
        <div className="absolute inset-[calc(26%-0.48px)_calc(86.29%+0.73px)_calc(26%-0.48px)_calc(6.82%-0.86px)]" data-node-id="601:792" data-name="🦆 icon 'search'">
          <div className="absolute inset-[-3.13%_-3.12%_-3.13%_-3.13%]">
            <img alt="" className="block max-w-none size-full" src={imgIconSearch} />
          </div>
        </div>
        <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Light'] leading-[18px] left-[137px] not-italic text-[14px] text-[rgba(6,0,79,0.6)] text-center top-[15px] tracking-[-0.165px] whitespace-nowrap" data-node-id="601:795">
          what do you search for?
        </p>
      </div>
      <div className="absolute bg-[rgba(219,228,237,0.5)] border-[rgba(0,65,130,0.3)] border-b border-l border-solid border-t h-[724px] left-[16px] overflow-clip rounded-bl-[10px] rounded-br-[10px] rounded-tl-[10px] top-[157px] w-[137px]" data-node-id="601:799">
        <a className="-translate-x-1/2 [word-break:break-word] absolute block cursor-pointer font-['Poppins:Medium'] leading-[0] left-[67.5px] not-italic text-[#06004f] text-[14px] text-center top-[97px] tracking-[-0.165px] whitespace-nowrap" data-node-id="601:800">
          <p className="leading-[18px]">Women’s Fashion</p>
        </a>
        <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[36px] not-italic text-[#06004f] text-[14px] text-center top-[163px] tracking-[-0.165px] whitespace-nowrap" data-node-id="601:801">
          Skincare
        </p>
        <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[31.5px] not-italic text-[#06004f] text-[14px] text-center top-[229px] tracking-[-0.165px] whitespace-nowrap" data-node-id="601:802">
          Beauty
        </p>
        <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[39.5px] not-italic text-[#06004f] text-[14px] text-center top-[361px] tracking-[-0.165px] whitespace-nowrap" data-node-id="601:804">
          Cameras
        </p>
        <div className="[word-break:break-word] absolute font-['Poppins:Medium'] leading-[0] left-[7px] not-italic text-[#06004f] text-[14px] top-[427px] tracking-[-0.165px] whitespace-nowrap" data-node-id="601:805">
          <p className="leading-[18px] mb-0">{`Laptops &`}</p>
          <p className="leading-[18px]">Electronics</p>
        </div>
        <p className="[word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[7px] not-italic text-[#06004f] text-[14px] top-[511px] tracking-[-0.165px] whitespace-nowrap" data-node-id="601:806">{`Baby & Toys`}</p>
        <div className="absolute bg-white h-[82px] left-[-1px] overflow-clip rounded-tl-[10px] top-[-1px] w-[137px]" data-node-id="601:807">
          <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[66px] not-italic text-[#06004f] text-[14px] text-center top-[32px] tracking-[-0.165px] whitespace-nowrap" data-node-id="601:808">
            Men’s Fashion
          </p>
          <div className="absolute h-[66px] left-[4px] rounded-[10px] top-[9px] w-[4px]" data-node-id="601:809" />
          <div className="absolute bg-[#004182] h-[72px] left-[5px] rounded-[20px] top-[5px] w-[7px]" data-node-id="769:490" />
        </div>
      </div>
      <div className="absolute h-[94px] left-[calc(33.33%+33.67px)] overflow-clip rounded-[10px] top-[191px] w-[237px]" data-node-id="601:810">
        <div aria-hidden className="absolute inset-0 pointer-events-none rounded-[10px]">
          <div className="absolute bg-[#f0f0f0] inset-0 rounded-[10px]" />
          <img alt="" className="absolute max-w-none object-cover opacity-80 rounded-[10px] size-full" src={imgFrame32} />
        </div>
        <div className="[word-break:break-word] absolute font-['Poppins:SemiBold'] leading-[0] left-[16px] not-italic text-[#06004f] text-[16px] top-[8px] tracking-[-0.165px] whitespace-nowrap" data-node-id="601:811">
          <p className="leading-[20px] mb-0">Men’s</p>
          <p className="leading-[20px]">Fashion</p>
        </div>
        <div className="absolute bg-[#004182] h-[30px] left-[16px] overflow-clip rounded-[10px] top-[56px] w-[97px]" data-node-id="601:812">
          <p className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[14px] not-italic text-[12px] text-white top-[6px] tracking-[-0.165px] whitespace-nowrap" data-node-id="601:813">
            Shop Now
          </p>
        </div>
      </div>
      <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[calc(33.33%+82.67px)] not-italic text-[#06004f] text-[14px] text-center top-[157px] tracking-[-0.165px] whitespace-nowrap" data-node-id="601:814">
        Men’s Fashion
      </p>
      <div className="absolute border border-[rgba(0,65,130,0.3)] border-solid left-[calc(33.33%+33.67px)] rounded-[5px] size-[70px] top-[305px]" data-node-id="601:872">
        <img alt="" className="absolute inset-0 max-w-none object-cover pointer-events-none rounded-[5px] size-full" src={imgFrame47} />
      </div>
      <div className="absolute border border-[rgba(0,65,130,0.3)] border-solid left-[calc(33.33%+33.67px)] rounded-[5px] size-[70px] top-[417px]" data-node-id="601:875">
        <img alt="" className="absolute inset-0 max-w-none object-cover pointer-events-none rounded-[5px] size-full" src={imgFrame50} />
      </div>
      <a className="absolute block border border-[rgba(0,65,130,0.3)] border-solid cursor-pointer left-[calc(50%+45px)] rounded-[5px] size-[70px] top-[417px]" data-node-id="601:879">
        <img alt="" className="absolute inset-0 max-w-none object-cover pointer-events-none rounded-[5px] size-full" src={imgFrame54} />
      </a>
      <div className="absolute border border-[rgba(0,65,130,0.3)] border-solid left-[calc(66.67%+57.33px)] rounded-[5px] size-[70px] top-[417px]" data-node-id="601:880">
        <img alt="" className="absolute inset-0 max-w-none object-cover pointer-events-none rounded-[5px] size-full" src={imgFrame55} />
      </div>
      <div className="absolute border border-[rgba(0,65,130,0.3)] border-solid left-[calc(33.33%+33.67px)] rounded-[5px] size-[70px] top-[529px]" data-node-id="601:876">
        <img alt="" className="absolute inset-0 max-w-none object-cover pointer-events-none rounded-[5px] size-full" src={imgFrame51} />
      </div>
      <div className="absolute border border-[rgba(0,65,130,0.3)] border-solid left-[calc(50%+45px)] rounded-[5px] size-[70px] top-[529px]" data-node-id="601:877">
        <div className="absolute inset-0 overflow-hidden pointer-events-none rounded-[5px]">
          <img alt="" className="absolute h-[133.33%] left-[0.94%] max-w-none top-[-2.55%] w-full" src={imgFrame52} />
        </div>
      </div>
      <div className="absolute border border-[rgba(0,65,130,0.3)] border-solid left-[calc(66.67%+57.33px)] rounded-[5px] size-[70px] top-[529px]" data-node-id="601:878">
        <div className="absolute inset-0 overflow-hidden pointer-events-none rounded-[5px]">
          <img alt="" className="absolute h-[150%] left-[1.31%] max-w-none top-[-40.68%] w-full" src={imgFrame53} />
        </div>
      </div>
      <p className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[calc(33.33%+41.67px)] not-italic text-[#06004f] text-[12px] top-[383px] tracking-[-0.165px] whitespace-nowrap" data-node-id="601:881">
        T-shirts
      </p>
      <p className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[calc(50%+59px)] not-italic text-[#06004f] text-[12px] top-[383px] tracking-[-0.165px] whitespace-nowrap" data-node-id="601:882">
        Shorts
      </p>
      <p className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[calc(83.33%+0.67px)] not-italic text-[#06004f] text-[12px] top-[383px] tracking-[-0.165px] whitespace-nowrap" data-node-id="601:883">
        Jeans
      </p>
      <div className="absolute border border-[rgba(0,65,130,0.3)] border-solid left-[calc(50%+45px)] rounded-[5px] size-[70px] top-[305px]" data-node-id="601:873">
        <img alt="" className="absolute inset-0 max-w-none object-cover pointer-events-none rounded-[5px] size-full" src={imgFrame48} />
      </div>
      <div className="absolute border border-[rgba(0,65,130,0.3)] border-solid left-[calc(66.67%+57.33px)] rounded-[5px] size-[70px] top-[305px]" data-node-id="601:874">
        <img alt="" className="absolute inset-0 max-w-none object-cover pointer-events-none rounded-[5px] size-full" src={imgFrame49} />
      </div>
      <p className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[calc(33.33%+49.67px)] not-italic text-[#06004f] text-[12px] top-[495px] tracking-[-0.165px] whitespace-nowrap" data-node-id="601:884">
        Pants
      </p>
      <p className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[calc(50%+54px)] not-italic text-[#06004f] text-[12px] top-[495px] tracking-[-0.165px] whitespace-nowrap" data-node-id="601:885">
        Footwear
      </p>
      <p className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[calc(83.33%+7.67px)] not-italic text-[#06004f] text-[12px] top-[495px] tracking-[-0.165px] whitespace-nowrap" data-node-id="601:886">
        Suits
      </p>
      <p className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[calc(33.33%+39.67px)] not-italic text-[#06004f] text-[12px] top-[607px] tracking-[-0.165px] whitespace-nowrap" data-node-id="601:887">
        Watches
      </p>
      <p className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[calc(66.67%-8.67px)] not-italic text-[#06004f] text-[12px] top-[607px] tracking-[-0.165px] whitespace-nowrap" data-node-id="601:888">
        Bags
      </p>
      <p className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[calc(83.33%-13.33px)] not-italic text-[#06004f] text-[12px] top-[607px] tracking-[-0.165px] whitespace-nowrap" data-node-id="601:889">
        Eyewears
      </p>
      <a className="absolute block cursor-pointer left-[calc(83.33%+31.67px)] size-[24px] top-[104px]" data-node-id="620:1056" data-name="🦆 icon 'shopping cart'">
        <div className="absolute inset-[-4.17%]">
          <img alt="" className="block max-w-none size-full" src={imgIconShoppingCart} />
        </div>
      </a>
      <div className="absolute bg-[#004182] h-[56px] left-0 overflow-clip rounded-[15px] top-[876px] w-[430px]" data-node-id="1003:418">
        <div className="absolute left-[148px] size-[40px] top-[8px]" data-node-id="I1003:418;964:523">
          <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgEllipse42} />
        </div>
        <a className="absolute block cursor-pointer left-[242px] size-[40px] top-[8px]" data-node-id="I1003:418;964:482">
          <div className="absolute left-[8px] size-[24px] top-[8px]" data-node-id="I1003:418;964:482;769:622" data-name="heart (3) 3">
            <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgHeart33} />
          </div>
        </a>
        <div className="absolute left-[148px] size-[40px] top-[8px]" data-node-id="I1003:418;964:483">
          <div className="absolute inset-[20%]" data-node-id="I1003:418;964:483;769:611" data-name="category (1) 3">
            <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgCategory13} />
          </div>
        </div>
        <div className="absolute left-[55px] size-[40px] top-[8px]" data-node-id="I1003:418;964:484">
          <div className="absolute left-[8px] size-[24px] top-[8px]" data-node-id="I1003:418;964:484;769:590" data-name="home (1) 3">
            <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgHome13} />
          </div>
        </div>
        <a className="absolute block cursor-pointer left-[335px] size-[40px] top-[8px]" data-node-id="I1003:418;964:485">
          <div className="absolute left-[8px] size-[24px] top-[8px]" data-node-id="I1003:418;964:485;769:633" data-name="user (1) 3">
            <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgUser13} />
          </div>
        </a>
      </div>
    </div>
  );
}

SUPER CRITICAL: The generated React+Tailwind code MUST be converted to match the target project's technology stack and styling system.
1. Analyze the target codebase to identify: technology stack, styling approach, component patterns, and design tokens
2. Convert React syntax to the target framework/library
3. Transform all Tailwind classes to the target styling system while preserving exact visual design
4. Follow the project's existing patterns and conventions
DO NOT install any Tailwind as a dependency unless the user instructs you to do so.


Node ids have been added to the code as data attributes, e.g. `data-node-id="1:2"`.

These styles are contained in the design: description color: #06004F, text color: #06004F, main color: #004182, strok color: #004182, small text: Font(family: "Poppins", style: Regular, size: 12, weight: 400, lineHeight: 18, letterSpacing: -0.16500000655651093), white: #FFFFFF.

Images and SVGs will be stored as constants, e.g. const image = '../assets/550e8400-e29b-41d4-a716-446655440000.png'. These constants will be used in the code as the source for the image, ex: <img src={image} />. Image assets are stored on a remote server for 7 days and can be fetched using the provided URLs until they expire.
