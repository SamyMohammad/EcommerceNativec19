const imgIconPlusCircle = "../assets/e192b68e-83fc-4167-b283-731cbae1c5ba.svg";
const imgGroup = "../assets/98548da2-57e6-483d-8573-37c99b535259.svg";
const imgRectangle = "../assets/e4c783d4-f36a-41a3-806b-19a0bf7ce543.svg";
const imgCombinedShape = "../assets/3cfee89e-34d1-4149-a853-d3cc117bd877.svg";
const imgWiFi = "../assets/93824489-2964-4103-aa9f-7a73b5eb027e.svg";
const imgFrame65 = "../assets/7b7c4f3e-6e70-49f1-8a4a-f74f7b850645.png";
const imgEllipse32 = "../assets/54b3dce5-8389-4420-a326-11fef8222f7d.svg";
const imgDelete1 = "../assets/8c0124d1-2a01-4f82-813c-a83d0d4a25c3.svg";
const imgIconArrowBackOutline = "../assets/825c48ea-68fa-4450-8aa9-82c4a563a345.svg";
const imgIconShoppingCart = "../assets/5cdfe9f5-28a2-478b-bce5-62057ce18251.svg";
const imgIconSearch = "../assets/aaef27db-660a-4d71-8b1e-81b3f9338950.svg";
const imgArrow61 = "../assets/f7fe0b37-5460-42c9-b638-c48504f15f33.svg";

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

export default function Cart() {
  return (
    <div className="bg-white overflow-clip relative rounded-[20px] size-full" data-node-id="604:890" data-name="Cart">
      <StatusBar className="absolute bg-white h-[44px] left-px right-px rounded-[20px] top-0" />
      <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[calc(33.33%+71.67px)] not-italic text-[#06004f] text-[20px] text-center top-[56px] tracking-[-0.165px] whitespace-nowrap" dir="auto" data-node-id="604:995">
        Cart
      </p>
      <div className="absolute border border-[rgba(0,65,130,0.3)] border-solid h-[113px] left-[16px] overflow-clip rounded-[15px] top-[106px] w-[398px]" data-node-id="715:485">
        <div className="absolute border border-[rgba(0,65,130,0.3)] border-solid h-[113px] left-[-1px] rounded-[15px] top-[-1px] w-[120px]" data-node-id="715:486">
          <img alt="" className="absolute inset-0 max-w-none object-cover pointer-events-none rounded-[15px] size-full" src={imgFrame65} />
        </div>
        <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[194px] not-italic text-[#06004f] text-[18px] text-center top-[15px] tracking-[-0.165px] whitespace-nowrap" data-node-id="715:487">
          Nike Air Jordon
        </p>
        <div className="absolute left-[127px] size-[15px] top-[48px]" data-node-id="715:488">
          <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgEllipse32} />
        </div>
        <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[204.5px] not-italic text-[14px] text-[rgba(6,0,79,0.6)] text-center top-[46px] tracking-[-0.165px] whitespace-pre" data-node-id="715:489">{`Orange  Size: 40`}</p>
        <div className="absolute bg-[#6a6695] h-[15px] left-[204px] rounded-[20px] top-[48px] w-[2px]" data-node-id="715:490" />
        <div className="absolute left-[365px] size-[24px] top-[7px]" data-node-id="715:493" data-name="delete 1">
          <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgDelete1} />
        </div>
        <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[170.5px] not-italic text-[#06004f] text-[18px] text-center top-[80px] tracking-[-0.165px] whitespace-nowrap" data-node-id="718:505">
          EGP 3,500
        </p>
        <Frame62 className="absolute bg-[#004182] h-[42px] left-[267px] overflow-clip rounded-[20px] top-[62px] w-[122px]" />
      </div>
      <div className="absolute inset-[5.9%_90.7%_91.52%_3.72%]" data-node-id="620:1041" data-name="🦆 icon 'arrow back outline'">
        <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgIconArrowBackOutline} />
      </div>
      <div className="absolute left-[calc(83.33%+31.67px)] size-[24px] top-[55px]" data-node-id="620:1068" data-name="🦆 icon 'shopping cart'">
        <div className="absolute inset-[-4.17%]">
          <img alt="" className="block max-w-none size-full" src={imgIconShoppingCart} />
        </div>
      </div>
      <div className="absolute inset-[5.9%_16.74%_91.52%_77.67%]" data-node-id="715:482" data-name="🦆 icon 'search'">
        <div className="absolute inset-[-3.13%_-3.12%]">
          <img alt="" className="block max-w-none size-full" src={imgIconSearch} />
        </div>
      </div>
      <div className="absolute bg-[#004182] h-[48px] left-[calc(33.33%+0.67px)] overflow-clip rounded-[20px] top-[786px] w-[270px]" data-node-id="722:509">
        <p className="[word-break:break-word] absolute font-['Poppins:Medium'] inset-[31.25%_30.74%] leading-[18px] not-italic text-[20px] text-center text-white tracking-[-0.165px] whitespace-nowrap" dir="auto" data-node-id="722:510">
          Check Out
        </p>
        <div className="absolute bottom-1/4 left-[79.26%] right-[11.85%] top-1/4" data-node-id="722:517" data-name="arrow (6) 1">
          <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgArrow61} />
        </div>
      </div>
      <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[59.5px] not-italic text-[#06004f] text-[18px] text-center top-[816px] tracking-[-0.165px] whitespace-nowrap" data-node-id="722:520">
        EGP 3,500
      </p>
      <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[63.5px] not-italic text-[18px] text-[rgba(6,0,79,0.6)] text-center top-[786px] tracking-[-0.165px] whitespace-nowrap" dir="auto" data-node-id="722:521">
        Total price
      </p>
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

These styles are contained in the design: text color: #06004F, strok color: #004182, header 1: Font(family: "Poppins", style: Medium, size: 18, weight: 500, lineHeight: 18, letterSpacing: -0.16500000655651093), description color: #06004F, general text: Font(family: "Poppins", style: Regular, size: 14, weight: 400, lineHeight: 18, letterSpacing: -0.16500000655651093), main color: #004182, button: Font(family: "Poppins", style: Medium, size: 20, weight: 500, lineHeight: 18, letterSpacing: -0.16500000655651093).

Images and SVGs will be stored as constants, e.g. const image = '../assets/550e8400-e29b-41d4-a716-446655440000.png'. These constants will be used in the code as the source for the image, ex: <img src={image} />. Image assets are stored on a remote server for 7 days and can be fetched using the provided URLs until they expire.
