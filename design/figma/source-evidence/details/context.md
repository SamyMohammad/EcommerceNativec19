const imgIconPlusCircle = "../assets/6a221117-b8e3-4a05-b544-a390fc59d64e.svg";
const imgGroup = "../assets/7ca8f3d9-aef5-411c-8657-a061b86d5536.svg";
const imgProperty1Default = "../assets/1e9f332e-37bd-4c0e-b7f1-20591da1a058.svg";
const imgProperty1Default1 = "../assets/995b0786-4447-431c-8d10-07f6a9b84b80.png";
const imgGroup14 = "../assets/cbb31f9f-28ce-4eac-b52d-68350f0ec7cb.svg";
const imgRectangle = "../assets/f3234218-c522-4a96-964c-ba9f596d663d.svg";
const imgCombinedShape = "../assets/859b6be4-0acf-4e82-8d20-680db0b6c0f7.svg";
const imgWiFi = "../assets/781c49c5-66fd-413c-8627-07f9394c2765.svg";
const imgIconArrowBackOutline = "../assets/748fc219-7ee2-4d51-b95a-853a2f607932.svg";
const imgIconShoppingCart = "../assets/5fbd2eae-1d4e-4414-955c-07c69fbbdca0.svg";
const imgEmojiWhiteMediumStar = "../assets/340926fa-49e9-42f3-b37f-04ca0e5775a5.svg";
const imgEllipse21 = "../assets/f0ec639f-1dcc-4944-8d71-721dbdc4d25c.svg";
const imgEllipse24 = "../assets/dc73cc40-e521-4afa-b8b5-66054021d987.svg";
const imgEllipse25 = "../assets/6fc7e88b-c0fe-4175-9c8f-2133911ec9c4.svg";
const imgEllipse26 = "../assets/44d06de7-1c27-4270-a025-bab9abd2cdee.svg";
const imgEllipse29 = "../assets/a4c3cb3a-b155-41ee-a20c-63e205e2c579.svg";
const imgEllipse30 = "../assets/38ad4695-1d41-4dcd-a0a5-e1ce347c24f8.svg";
const imgEllipse22 = "../assets/f3e15d7f-44b4-441c-b812-8be91e33d789.svg";
const imgEllipse23 = "../assets/b0ccf0c6-81c0-4a37-b9ac-9bdd8ed354bb.svg";
const imgRight1 = "../assets/ebb623ae-a057-4c47-be0a-6e9dc9ab850e.svg";
const imgCart21 = "../assets/84081403-b98b-4ad7-b347-161995145f11.svg";
const imgIconSearch = "../assets/f2b4d17a-3a3c-44e0-949b-48c48c491b06.svg";

type Frame62Props = {
  className?: string;
  property1?: "Default";
};

function Frame62({ className, property1 = "Default" }: Frame62Props) {
  return (
    <div className={className || "bg-[#004182] h-[42px] overflow-clip relative rounded-[20px] w-[122px]"} data-node-id="727:1046">
      <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[61.5px] not-italic text-[18px] text-center text-white top-[12px] tracking-[-0.165px] whitespace-nowrap" data-node-id="727:1037">
        1
      </p>
      <button className="absolute block cursor-pointer inset-[26.19%_13.11%_26.19%_70.49%]" data-node-id="727:1038" data-name="🦆 icon 'plus circle'">
        <div className="absolute inset-[-5%]">
          <img alt="" className="block max-w-none size-full" src={imgIconPlusCircle} />
        </div>
      </button>
      <div className="absolute contents inset-[26.19%_70.49%_26.19%_13.11%]" data-node-id="727:1042" data-name="🦆 icon 'subtract circle minus remove'">
        <div className="absolute inset-[26.19%_70.49%_26.19%_13.11%]" data-node-id="727:1043" data-name="Group">
          <div className="absolute inset-[-5%]">
            <img alt="" className="block max-w-none size-full" src={imgGroup} />
          </div>
        </div>
      </div>
    </div>
  );
}

type Group17Props = {
  className?: string;
  property1?: "Default";
};

function Group17({ className, property1 = "Default" }: Group17Props) {
  return (
    <button className={className || "block relative size-[30px]"} data-node-id="726:842">
      <div className="absolute inset-[-16.67%_-33.33%_-50%_-33.33%]">
        <img alt="" className="block max-w-none size-full" src={imgProperty1Default} />
      </div>
    </button>
  );
}

type Frame58Props = {
  className?: string;
  property1?: "Default";
};

function Frame58({ className, property1 = "Default" }: Frame58Props) {
  return (
    <div className={className || "border border-[rgba(0,65,130,0.3)] border-solid h-[300px] overflow-clip relative rounded-[15px] w-[398px]"} data-node-id="604:1012">
      <img alt="" className="absolute inset-0 max-w-none object-cover pointer-events-none rounded-[15px] size-full" src={imgProperty1Default1} />
      <div className="absolute contents left-[169px] top-[284px]" data-node-id="620:1152">
        <div className="absolute h-[7px] left-[210px] top-[285px] w-[20px]" data-node-id="604:1003">
          <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgGroup14} />
        </div>
        <div className="absolute bg-[#004182] h-[7px] left-[170px] rounded-[30px] top-[285px] w-[30px]" data-node-id="620:1143" />
      </div>
      <Group17 className="absolute block cursor-pointer left-[351px] size-[30px] top-[15px]" />
    </div>
  );
}

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

export default function ProductDetails() {
  return (
    <div className="bg-white overflow-clip relative rounded-[20px] size-full" data-node-id="620:1083" data-name="Product Details">
      <StatusBar className="absolute bg-white h-[44px] left-px right-px rounded-[20px] top-0" />
      <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[calc(33.33%+71.67px)] not-italic text-[#06004f] text-[20px] text-center top-[56px] tracking-[-0.165px] whitespace-nowrap" data-node-id="620:1086">
        Product Details
      </p>
      <Frame58 className="absolute border border-[rgba(0,65,130,0.3)] border-solid h-[300px] left-[16px] overflow-clip rounded-[15px] top-[90px] w-[398px]" />
      <a className="absolute block cursor-pointer inset-[5.9%_90.7%_91.52%_3.72%]" data-node-id="620:1088" data-name="🦆 icon 'arrow back outline'">
        <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgIconArrowBackOutline} />
      </a>
      <a className="absolute block cursor-pointer left-[calc(83.33%+31.67px)] size-[24px] top-[55px]" data-node-id="620:1091" data-name="🦆 icon 'shopping cart'">
        <div className="absolute inset-[-4.17%]">
          <img alt="" className="block max-w-none size-full" src={imgIconShoppingCart} />
        </div>
      </a>
      <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[83px] not-italic text-[#06004f] text-[18px] text-center top-[414px] tracking-[-0.165px] whitespace-nowrap" data-node-id="620:1138">
        Nike Air Jordon
      </p>
      <div className="absolute border border-[rgba(0,65,130,0.3)] border-solid h-[34px] left-[16px] overflow-clip rounded-[20px] top-[448px] w-[102px]" data-node-id="622:1233">
        <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[50.5px] not-italic text-[#06004f] text-[14px] text-center top-[7.5px] tracking-[-0.165px] whitespace-nowrap" data-node-id="622:1234">
          3,230 Sold
        </p>
      </div>
      <div className="absolute inset-[49.03%_65.35%_49.36%_31.16%]" data-node-id="60:69" data-name="🦆 emoji 'white medium star'">
        <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgEmojiWhiteMediumStar} />
      </div>
      <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[calc(33.33%+46.17px)] not-italic text-[#06004f] text-[14px] text-center top-[456px] tracking-[-0.165px] whitespace-nowrap" data-node-id="622:1235">
        4.8 (7,500)
      </p>
      <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[59.5px] not-italic text-[#06004f] text-[18px] text-center top-[810px] tracking-[-0.165px] whitespace-nowrap" data-node-id="704:381">
        EGP 3,500
      </p>
      <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[63.5px] not-italic text-[18px] text-[rgba(6,0,79,0.6)] text-center top-[780px] tracking-[-0.165px] whitespace-nowrap" dir="auto" data-node-id="704:382">
        Total price
      </p>
      <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[67px] not-italic text-[#06004f] text-[18px] text-center top-[498px] tracking-[-0.165px] whitespace-nowrap" data-node-id="694:343">
        Description
      </p>
      <p className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[0] left-[16px] not-italic text-[14px] text-[rgba(6,0,79,0.6)] top-[524px] tracking-[-0.165px] w-[381px]" data-node-id="695:345">
        <span className="leading-[18px]">Nike is a multinational corporation that designs, develops, and sells athletic footwear ,apparel, and accessories......</span>
        <span className="leading-[18px] text-[#06004f]">{`Read More `}</span>
      </p>
      <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[33.5px] not-italic text-[#06004f] text-[18px] text-center top-[594px] tracking-[-0.165px] whitespace-nowrap" data-node-id="696:347">{`Size `}</p>
      <div className="absolute left-[16px] size-[35px] top-[620px]" data-node-id="696:360">
        <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgEllipse21} />
      </div>
      <div className="absolute left-[calc(33.33%+1.67px)] size-[35px] top-[620px]" data-node-id="709:416">
        <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgEllipse21} />
      </div>
      <div className="absolute left-[16px] size-[35px] top-[697px]" data-node-id="696:370">
        <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgEllipse24} />
      </div>
      <div className="absolute left-[59px] size-[35px] top-[697px]" data-node-id="696:372">
        <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgEllipse25} />
      </div>
      <div className="absolute left-[calc(16.67%+30.33px)] size-[35px] top-[697px]" data-node-id="696:373">
        <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgEllipse26} />
      </div>
      <div className="absolute left-[calc(33.33%+1.67px)] size-[35px] top-[697px]" data-node-id="709:420">
        <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgEllipse29} />
      </div>
      <div className="absolute left-[calc(33.33%+44.67px)] size-[35px] top-[697px]" data-node-id="709:421">
        <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgEllipse30} />
      </div>
      <div className="absolute left-[59px] size-[35px] top-[620px]" data-node-id="696:363">
        <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgEllipse22} />
      </div>
      <div className="absolute left-[calc(33.33%+44.67px)] size-[35px] top-[620px]" data-node-id="709:417">
        <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgEllipse22} />
      </div>
      <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[33.5px] not-italic text-[#06004f] text-[14px] text-center top-[629px] tracking-[-0.165px] whitespace-nowrap" data-node-id="696:365">
        38
      </p>
      <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[calc(33.33%+18.67px)] not-italic text-[#06004f] text-[14px] text-center top-[629px] tracking-[-0.165px] whitespace-nowrap" data-node-id="709:418">
        41
      </p>
      <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[76.5px] not-italic text-[#06004f] text-[14px] text-center top-[629px] tracking-[-0.165px] whitespace-nowrap" data-node-id="696:366">
        39
      </p>
      <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[calc(33.33%+62.17px)] not-italic text-[#06004f] text-[14px] text-center top-[629px] tracking-[-0.165px] whitespace-nowrap" data-node-id="709:419">
        42
      </p>
      <div className="absolute left-[calc(16.67%+30.33px)] size-[35px] top-[620px]" data-node-id="696:364">
        <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgEllipse23} />
      </div>
      <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[calc(16.67%+47.33px)] not-italic text-[14px] text-center text-white top-[629px] tracking-[-0.165px] whitespace-nowrap" data-node-id="696:368">
        40
      </p>
      <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[40px] not-italic text-[#06004f] text-[18px] text-center top-[671px] tracking-[-0.165px] whitespace-nowrap" dir="auto" data-node-id="696:369">
        Color
      </p>
      <div className="absolute left-[67px] size-[20px] top-[705px]" data-node-id="701:377" data-name="right 1">
        <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgRight1} />
      </div>
      <a className="absolute bg-[#004182] block cursor-pointer h-[48px] left-[calc(33.33%+0.67px)] overflow-clip rounded-[20px] top-[780px] w-[270px]" data-node-id="704:380">
        <p className="[word-break:break-word] absolute font-['Poppins:Medium'] inset-[31.25%_29.26%_31.25%_29.63%] leading-[18px] not-italic text-[20px] text-center text-white tracking-[-0.165px] whitespace-nowrap" data-node-id="704:404">
          Add to cart
        </p>
        <div className="absolute bottom-1/4 left-[11.85%] right-[79.26%] top-1/4" data-node-id="707:412" data-name="cart (2) 1">
          <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgCart21} />
        </div>
      </a>
      <div className="absolute inset-[5.9%_16.74%_91.52%_77.67%]" data-node-id="709:422" data-name="🦆 icon 'search'">
        <div className="absolute inset-[-3.13%_-3.12%]">
          <img alt="" className="block max-w-none size-full" src={imgIconSearch} />
        </div>
      </div>
      <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[calc(66.67%+83.83px)] not-italic text-[#06004f] text-[18px] text-center top-[414px] tracking-[-0.165px] whitespace-nowrap" data-node-id="622:1302">
        EGP 3,500
      </p>
      <Frame62 className="absolute bg-[#004182] h-[42px] left-[calc(66.67%+5.33px)] overflow-clip rounded-[20px] top-[448px] w-[122px]" />
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

These styles are contained in the design: text color: #06004F, main color: #004182, white: #FFFFFF, strok color: #004182, header 1: Font(family: "Poppins", style: Medium, size: 18, weight: 500, lineHeight: 18, letterSpacing: -0.16500000655651093), sub-category: Font(family: "Poppins", style: Medium, size: 14, weight: 500, lineHeight: 18, letterSpacing: -0.16500000655651093), general text: Font(family: "Poppins", style: Regular, size: 14, weight: 400, lineHeight: 18, letterSpacing: -0.16500000655651093), description color: #06004F, button: Font(family: "Poppins", style: Medium, size: 20, weight: 500, lineHeight: 18, letterSpacing: -0.16500000655651093).

Images and SVGs will be stored as constants, e.g. const image = '../assets/550e8400-e29b-41d4-a716-446655440000.png'. These constants will be used in the code as the source for the image, ex: <img src={image} />. Image assets are stored on a remote server for 7 days and can be fetched using the provided URLs until they expire.
