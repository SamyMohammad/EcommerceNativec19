const imgProperty1Default = "../assets/4b4ae607-67b6-48a7-bc59-0e1d670e12cc.svg";
const imgProperty1Variant2 = "../assets/cdba9644-3e09-4305-beea-68f6b90fcc53.svg";
const imgRectangle = "../assets/f6488c7b-4fe0-4a66-820a-c46060121a15.svg";
const imgCombinedShape = "../assets/57303bb1-8c62-4b6f-a114-b9175dcf2e63.svg";
const imgWiFi = "../assets/a7c24ccd-0834-4dd1-892f-2f0174a9c7cb.svg";
const imgFrame65 = "../assets/2ac7cd2d-28ae-4507-bb90-8808a97f477f.png";
const imgFrame66 = "../assets/4cff3886-5541-48ba-9c7f-3d7f75dd04b1.png";
const imgFrame67 = "../assets/70608138-1595-44ae-a6ff-8dd7397f7898.png";
const imgGroup5 = "../assets/39cbc8ea-8fca-49d5-9dff-a6e66dd3b315.svg";
const imgIconSearch = "../assets/80b10f24-26f3-411d-986a-a1178f7791de.svg";
const imgIconShoppingCart = "../assets/a5a498fe-cd54-430c-952d-7fcd05e23fbb.svg";
const imgEllipse32 = "../assets/ef0dac02-c81a-44ac-a6f3-15eafd673542.svg";
const imgLine1 = "../assets/ba6c062c-c46d-490f-ad30-73b167ae07ab.svg";
const imgEllipse33 = "../assets/fdb37489-24a6-42e7-a447-1ae3bcab1cfa.svg";
const imgEllipse34 = "../assets/35ce4d52-5620-449a-9c58-df728c9e01ac.svg";
const imgEllipse41 = "../assets/d24c52f2-3a32-4da6-8183-c6c726d5076e.svg";
const imgHeart33 = "../assets/19558981-fc51-493c-8d5f-7342df4de7f1.svg";
const imgCategory13 = "../assets/9ad5a473-b777-47c3-a59a-1a4b179b39fb.svg";
const imgHome13 = "../assets/46e6208a-c7c7-452a-8729-488b2fc61d03.svg";
const imgUser13 = "../assets/658b27aa-8299-44d0-ac08-a77afb028ff9.svg";

type Group17Props = {
  className?: string;
  property1?: "Default" | "Variant2";
};

function Group17({ className, property1 = "Default" }: Group17Props) {
  const isVariant2 = property1 === "Variant2";
  return (
    <button className={className || "block relative size-[30px]"} id={isVariant2 ? "node-726_844" : "node-726_842"}>
      <div className="absolute inset-[-16.67%_-33.33%_-50%_-33.33%]">
        <img alt="" className="block max-w-none size-full" src={isVariant2 ? imgProperty1Variant2 : imgProperty1Default} />
      </div>
    </button>
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

export default function Wishlist() {
  return (
    <div className="bg-white overflow-clip relative rounded-[20px] size-full" data-node-id="731:1294" data-name="Wishlist">
      <StatusBar className="absolute bg-white h-[44px] left-px right-px rounded-[20px] top-0" />
      <div className="absolute h-[22px] left-[16px] top-[50px] w-[66px]" data-node-id="731:1296">
        <div className="absolute inset-[-3.41%_-1.14%_-5.64%_-1.14%]">
          <img alt="" className="block max-w-none size-full" src={imgGroup5} />
        </div>
      </div>
      <div className="absolute bg-[rgba(255,255,255,0.7)] border border-[#004182] border-solid h-[50px] left-[16px] overflow-clip rounded-[25px] top-[91px] w-[348px]" data-node-id="731:1306">
        <div className="absolute inset-[calc(26%-0.48px)_calc(86.19%+0.72px)_calc(26%-0.48px)_calc(6.92%-0.86px)]" data-node-id="731:1307" data-name="🦆 icon 'search'">
          <div className="absolute inset-[-3.13%_-3.12%_-3.13%_-3.13%]">
            <img alt="" className="block max-w-none size-full" src={imgIconSearch} />
          </div>
        </div>
        <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Light'] leading-[18px] left-[137px] not-italic text-[14px] text-[rgba(6,0,79,0.6)] text-center top-[15px] tracking-[-0.165px] whitespace-nowrap" data-node-id="731:1310">
          what do you search for?
        </p>
      </div>
      <a className="absolute block cursor-pointer left-[calc(83.33%+31.67px)] size-[24px] top-[104px]" data-node-id="731:1311" data-name="🦆 icon 'shopping cart'">
        <div className="absolute inset-[-4.17%]">
          <img alt="" className="block max-w-none size-full" src={imgIconShoppingCart} />
        </div>
      </a>
      <div className="absolute border border-[rgba(0,65,130,0.3)] border-solid h-[113px] left-[16px] overflow-clip rounded-[15px] top-[165px] w-[398px]" data-node-id="731:1460">
        <div className="absolute border border-[rgba(0,65,130,0.3)] border-solid h-[113px] left-[-1px] rounded-[15px] top-[-1px] w-[120px]" data-node-id="731:1461">
          <img alt="" className="absolute inset-0 max-w-none object-cover pointer-events-none rounded-[15px] size-full" src={imgFrame65} />
        </div>
        <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[194px] not-italic text-[#06004f] text-[18px] text-center top-[13px] tracking-[-0.165px] whitespace-nowrap" data-node-id="731:1462">
          Nike Air Jordon
        </p>
        <div className="absolute left-[127px] size-[15px] top-[47px]" data-node-id="731:1463">
          <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgEllipse32} />
        </div>
        <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[205px] not-italic text-[14px] text-[rgba(6,0,79,0.6)] text-center top-[46px] tracking-[-0.165px] whitespace-pre" data-node-id="731:1464">{`  `}</p>
        <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[168px] not-italic text-[#06004f] text-[18px] text-center top-[78px] tracking-[-0.165px] whitespace-nowrap" data-node-id="731:1478">
          EGP 1,200
        </p>
        <div className="absolute bg-[#004182] h-[36px] left-[281px] overflow-clip rounded-[15px] top-[68px] w-[100px]" data-node-id="751:396">
          <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[50px] not-italic text-[14px] text-center text-white top-[9px] tracking-[-0.165px] whitespace-nowrap" dir="auto" data-node-id="751:397">
            Add to Cart
          </p>
        </div>
        <Group17 className="absolute block cursor-pointer left-[351px] size-[30px] top-[7px]" property1="Variant2" />
        <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[187.5px] not-italic text-[#06004f] text-[14px] text-center top-[45px] tracking-[-0.165px] whitespace-nowrap" data-node-id="754:402">
          Black color
        </p>
        <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[241.5px] not-italic text-[11px] text-[rgba(6,0,79,0.6)] text-center top-[80px] tracking-[-0.165px] whitespace-nowrap" dir="auto" data-node-id="759:403">
          EGP 1,500
        </p>
        <div className="absolute h-0 left-[217px] top-[90px] w-[49px]" data-node-id="759:404">
          <div className="absolute inset-[-1px_0_0_0]">
            <img alt="" className="block max-w-none size-full" src={imgLine1} />
          </div>
        </div>
      </div>
      <div className="absolute border border-[rgba(0,65,130,0.3)] border-solid h-[113px] left-[16px] overflow-clip rounded-[15px] top-[302px] w-[398px]" data-node-id="759:405">
        <div className="absolute border border-[rgba(0,65,130,0.3)] border-solid h-[113px] left-[-1px] rounded-[15px] top-[-1px] w-[120px]" data-node-id="759:406">
          <div className="absolute inset-0 overflow-hidden pointer-events-none rounded-[15px]">
            <img alt="" className="absolute h-[128.4%] left-[0.6%] max-w-none top-[-17.8%] w-full" src={imgFrame66} />
          </div>
        </div>
        <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[202.5px] not-italic text-[#06004f] text-[18px] text-center top-[13px] tracking-[-0.165px] whitespace-nowrap" data-node-id="759:407">
          Tall Cotton Dress
        </p>
        <div className="absolute left-[127px] size-[15px] top-[47px]" data-node-id="759:408">
          <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgEllipse33} />
        </div>
        <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[205px] not-italic text-[14px] text-[rgba(6,0,79,0.6)] text-center top-[46px] tracking-[-0.165px] whitespace-pre" data-node-id="759:409">{`  `}</p>
        <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[163.5px] not-italic text-[#06004f] text-[18px] text-center top-[78px] tracking-[-0.165px] whitespace-nowrap" data-node-id="759:410">
          EGP 600
        </p>
        <div className="absolute bg-[#004182] h-[36px] left-[281px] overflow-clip rounded-[15px] top-[68px] w-[100px]" data-node-id="759:411">
          <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[50px] not-italic text-[14px] text-center text-white top-[9px] tracking-[-0.165px] whitespace-nowrap" dir="auto" data-node-id="759:412">
            Add to Cart
          </p>
        </div>
        <Group17 className="absolute block cursor-pointer left-[351px] size-[30px] top-[7px]" property1="Variant2" />
        <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[195px] not-italic text-[#06004f] text-[14px] text-center top-[45px] tracking-[-0.165px] whitespace-nowrap" data-node-id="759:414">
          Orange color
        </p>
        <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[229.5px] not-italic text-[11px] text-[rgba(6,0,79,0.6)] text-center top-[80px] tracking-[-0.165px] whitespace-nowrap" dir="auto" data-node-id="759:415">
          EGP 750
        </p>
        <div className="absolute h-0 left-[205px] top-[90px] w-[49px]" data-node-id="759:416">
          <div className="absolute inset-[-1px_0_0_0]">
            <img alt="" className="block max-w-none size-full" src={imgLine1} />
          </div>
        </div>
      </div>
      <div className="absolute border border-[rgba(0,65,130,0.3)] border-solid h-[113px] left-[16px] overflow-clip rounded-[15px] top-[452px] w-[398px]" data-node-id="759:421">
        <div className="absolute border border-[rgba(0,65,130,0.3)] border-solid h-[113px] left-[-1px] rounded-[15px] top-[-1px] w-[120px]" data-node-id="759:422">
          <div className="absolute inset-0 overflow-hidden pointer-events-none rounded-[15px]">
            <img alt="" className="absolute h-[141.59%] left-[-0.56%] max-w-none top-[1.24%] w-full" src={imgFrame67} />
          </div>
        </div>
        <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[200px] not-italic text-[#06004f] text-[18px] text-center top-[13px] tracking-[-0.165px] whitespace-nowrap" data-node-id="759:423">
          GUESS Women’s
        </p>
        <div className="absolute left-[127px] size-[15px] top-[47px]" data-node-id="759:424">
          <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgEllipse34} />
        </div>
        <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[205px] not-italic text-[14px] text-[rgba(6,0,79,0.6)] text-center top-[46px] tracking-[-0.165px] whitespace-pre" data-node-id="759:425">{`  `}</p>
        <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[168px] not-italic text-[#06004f] text-[18px] text-center top-[78px] tracking-[-0.165px] whitespace-nowrap" data-node-id="759:426">
          EGP 1,200
        </p>
        <div className="absolute bg-[#004182] h-[36px] left-[281px] overflow-clip rounded-[15px] top-[68px] w-[100px]" data-node-id="759:427">
          <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[50px] not-italic text-[14px] text-center text-white top-[9px] tracking-[-0.165px] whitespace-nowrap" dir="auto" data-node-id="759:428">
            Add to Cart
          </p>
        </div>
        <Group17 className="absolute block cursor-pointer left-[351px] size-[30px] top-[7px]" property1="Variant2" />
        <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[190.5px] not-italic text-[#06004f] text-[14px] text-center top-[45px] tracking-[-0.165px] whitespace-nowrap" data-node-id="759:430">
          Gold Watch
        </p>
        <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[241.5px] not-italic text-[11px] text-[rgba(6,0,79,0.6)] text-center top-[80px] tracking-[-0.165px] whitespace-nowrap" dir="auto" data-node-id="759:431">
          EGP 1,500
        </p>
        <div className="absolute h-0 left-[217px] top-[90px] w-[49px]" data-node-id="759:432">
          <div className="absolute inset-[-1px_0_0_0]">
            <img alt="" className="block max-w-none size-full" src={imgLine1} />
          </div>
        </div>
      </div>
      <div className="absolute bg-[#004182] h-[56px] left-0 overflow-clip rounded-[15px] top-[876px] w-[430px]" data-node-id="1009:464">
        <div className="absolute left-[242px] size-[40px] top-[7px]" data-node-id="I1009:464;964:522">
          <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgEllipse41} />
        </div>
        <div className="absolute left-[242px] size-[40px] top-[8px]" data-node-id="I1009:464;964:495">
          <div className="absolute left-[8px] size-[24px] top-[8px]" data-node-id="I1009:464;964:495;769:622" data-name="heart (3) 3">
            <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgHeart33} />
          </div>
        </div>
        <a className="absolute block cursor-pointer left-[148px] size-[40px] top-[8px]" data-node-id="I1009:464;964:496">
          <div className="absolute inset-[20%]" data-node-id="I1009:464;964:496;769:611" data-name="category (1) 3">
            <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgCategory13} />
          </div>
        </a>
        <div className="absolute left-[55px] size-[40px] top-[8px]" data-node-id="I1009:464;964:497">
          <div className="absolute left-[8px] size-[24px] top-[8px]" data-node-id="I1009:464;964:497;769:590" data-name="home (1) 3">
            <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgHome13} />
          </div>
        </div>
        <a className="absolute block cursor-pointer left-[335px] size-[40px] top-[8px]" data-node-id="I1009:464;964:498">
          <div className="absolute left-[8px] size-[24px] top-[8px]" data-node-id="I1009:464;964:498;769:633" data-name="user (1) 3">
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

These styles are contained in the design: description color: #06004F, strok color: #004182, text color: #06004F, header 1: Font(family: "Poppins", style: Medium, size: 18, weight: 500, lineHeight: 18, letterSpacing: -0.16500000655651093), general text: Font(family: "Poppins", style: Regular, size: 14, weight: 400, lineHeight: 18, letterSpacing: -0.16500000655651093), sub-category: Font(family: "Poppins", style: Medium, size: 14, weight: 500, lineHeight: 18, letterSpacing: -0.16500000655651093), main color: #004182, white: #FFFFFF, old price: Font(family: "Poppins", style: Regular, size: 11, weight: 400, lineHeight: 18, letterSpacing: -0.16500000655651093).

Images and SVGs will be stored as constants, e.g. const image = '../assets/550e8400-e29b-41d4-a716-446655440000.png'. These constants will be used in the code as the source for the image, ex: <img src={image} />. Image assets are stored on a remote server for 7 days and can be fetched using the provided URLs until they expire.
