const imgProperty1Default = "../assets/f0cffe88-bd7b-4f53-bc28-ce5d21fb1fa8.svg";
const imgRectangle = "../assets/fac167b0-f138-40ee-93a2-2465487cd89a.svg";
const imgCombinedShape = "../assets/0ca9f31d-c79e-4d27-8459-eab487b1ec8f.svg";
const imgWiFi = "../assets/143ee94e-84ae-4c07-a4b3-45be7c364030.svg";
const imgFrame68 = "../assets/80e9b516-ced0-4d59-91bd-5c0f7b371663.png";
const imgFrame69 = "../assets/80e90f22-781d-47b7-9f60-a112f3d4ca3b.png";
const imgFrame70 = "../assets/3f7a88e3-9598-4c1e-81bf-17418af84f0f.png";
const imgFrame71 = "../assets/25193b09-2097-494c-b4df-dd4313bddea7.png";
const imgFrame72 = "../assets/d0f73343-8786-48c0-872f-31be14211102.png";
const imgFrame73 = "../assets/b6c21927-3580-4010-b4d0-f2536aa054a8.png";
const imgGroup5 = "../assets/82189557-6a05-4168-afdb-3b1b7c4c3ba5.svg";
const imgIconSearch = "../assets/064cf512-f3f7-4cbe-b5bb-8d4e9d9957b0.svg";
const imgIconShoppingCart = "../assets/16f4bf76-5df0-497d-807a-644514bfd1ac.svg";
const imgLine1 = "../assets/7e810729-e5c9-4e95-b77c-b433988b11f7.svg";
const imgIconPlusCircle = "../assets/0cb66c69-591d-4825-91ad-6b6faf472e3d.svg";
const imgEmojiWhiteMediumStar = "../assets/3a76c612-02e9-40cf-a031-d9b96aabf94a.svg";
const imgLine2 = "../assets/9c633c92-baaa-46ec-9b70-c696457fad45.svg";
const imgEllipse42 = "../assets/62d89833-6588-44f6-b2c1-83de9081106d.svg";
const imgHeart33 = "../assets/7057a929-f564-47ae-b99c-9c0e99f13498.svg";
const imgCategory13 = "../assets/03146784-057e-4970-b2ad-d1e798f47e7f.svg";
const imgHome13 = "../assets/2b41cef6-3559-4dac-8c0f-fb0e487ed085.svg";
const imgUser13 = "../assets/5ac4514d-c50c-423a-b5a9-d184898f182d.svg";

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

export default function ProductList() {
  return (
    <div className="bg-white overflow-clip relative rounded-[20px] size-full" data-node-id="726:650" data-name="Product list">
      <StatusBar className="absolute bg-white h-[44px] left-px right-px rounded-[20px] top-0" />
      <div className="absolute h-[22px] left-[16px] top-[50px] w-[66px]" data-node-id="726:663">
        <div className="absolute inset-[-3.41%_-1.14%_-5.64%_-1.14%]">
          <img alt="" className="block max-w-none size-full" src={imgGroup5} />
        </div>
      </div>
      <div className="absolute bg-[rgba(255,255,255,0.7)] border border-[#004182] border-solid h-[50px] left-[16px] overflow-clip rounded-[25px] top-[91px] w-[348px]" data-node-id="726:673">
        <div className="absolute inset-[calc(26%-0.48px)_calc(86.19%+0.72px)_calc(26%-0.48px)_calc(6.92%-0.86px)]" data-node-id="726:674" data-name="🦆 icon 'search'">
          <div className="absolute inset-[-3.13%_-3.12%_-3.13%_-3.13%]">
            <img alt="" className="block max-w-none size-full" src={imgIconSearch} />
          </div>
        </div>
        <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Light'] leading-[18px] left-[137px] not-italic text-[14px] text-[rgba(6,0,79,0.6)] text-center top-[15px] tracking-[-0.165px] whitespace-nowrap" data-node-id="726:677">
          what do you search for?
        </p>
      </div>
      <a className="absolute block cursor-pointer left-[calc(83.33%+31.67px)] size-[24px] top-[104px]" data-node-id="726:702" data-name="🦆 icon 'shopping cart'">
        <div className="absolute inset-[-4.17%]">
          <img alt="" className="block max-w-none size-full" src={imgIconShoppingCart} />
        </div>
      </a>
      <div className="absolute border-2 border-[rgba(0,65,130,0.3)] border-solid h-[237px] left-[16px] overflow-clip rounded-[15px] top-[165px] w-[191px]" data-node-id="727:1082">
        <div className="absolute h-[128px] left-[-2px] overflow-clip rounded-tl-[15px] rounded-tr-[15px] top-[-2px] w-[191px]" data-node-id="727:1087">
          <img alt="" className="absolute inset-0 max-w-none object-cover pointer-events-none rounded-tl-[15px] rounded-tr-[15px] size-full" src={imgFrame68} />
          <Group17 className="absolute block cursor-pointer left-[153px] size-[30px] top-[8px]" />
        </div>
        <div className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[0] left-[6px] not-italic text-[#06004f] text-[14px] top-[134px] tracking-[-0.165px] whitespace-nowrap" data-node-id="727:1110">
          <p className="leading-[18px] mb-0">Nike Air Jordon</p>
          <p className="leading-[18px]">Nike shoes flexible for wo..</p>
        </div>
        <p className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[6px] not-italic text-[#06004f] text-[14px] top-[178px] tracking-[-0.165px] whitespace-nowrap" dir="auto" data-node-id="727:1111">{`EGP 1,200 `}</p>
        <p className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[84px] not-italic text-[11px] text-[rgba(0,65,130,0.6)] top-[178px] tracking-[-0.165px] whitespace-nowrap" data-node-id="727:1112">
          2000 EGP
        </p>
        <div className="absolute h-0 left-[84px] top-[188px] w-[49px]" data-node-id="727:1113">
          <div className="absolute inset-[-1px_0_0_0]">
            <img alt="" className="block max-w-none size-full" src={imgLine1} />
          </div>
        </div>
        <div className="absolute inset-[calc(83.97%+1.36px)_calc(4.19%-1.83px)_calc(3.38%-1.86px)_calc(80.1%+1.2px)]" data-node-id="727:1098" data-name="🦆 icon 'plus circle'">
          <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgIconPlusCircle} />
        </div>
        <p className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[51px] not-italic text-[#06004f] text-[12px] top-[204px] tracking-[-0.165px] whitespace-nowrap" data-node-id="727:1101">
          (4.6)
        </p>
        <div className="absolute inset-[calc(86.92%+1.48px)_calc(47.64%-0.09px)_calc(6.75%-1.73px)_calc(44.5%-0.22px)]" data-node-id="727:1102" data-name="🦆 emoji 'white medium star'">
          <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgEmojiWhiteMediumStar} />
        </div>
        <p className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[6px] not-italic text-[#06004f] text-[12px] top-[204px] tracking-[-0.165px] whitespace-nowrap" data-node-id="727:1106">
          Review
        </p>
      </div>
      <a className="absolute block border-2 border-[rgba(0,65,130,0.3)] border-solid cursor-pointer h-[237px] left-[16px] overflow-clip rounded-[15px] top-[418px] w-[191px]" data-node-id="727:1128">
        <div className="absolute h-[128px] left-[-2px] overflow-clip rounded-tl-[15px] rounded-tr-[15px] top-[-2px] w-[191px]" data-node-id="727:1129">
          <img alt="" className="absolute inset-0 max-w-none object-cover pointer-events-none rounded-tl-[15px] rounded-tr-[15px] size-full" src={imgFrame69} />
          <Group17 className="absolute cursor-pointer left-[153px] size-[30px] top-[8px]" />
        </div>
        <div className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[0] left-[6px] not-italic text-[#06004f] text-[14px] text-left top-[134px] tracking-[-0.165px] whitespace-nowrap" data-node-id="727:1130">
          <p className="leading-[18px] mb-0">Nike Air Jordon</p>
          <p className="leading-[18px]">Nike shoes flexible for wo..</p>
        </div>
        <p className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[6px] not-italic text-[#06004f] text-[14px] text-left top-[178px] tracking-[-0.165px] whitespace-nowrap" dir="auto" data-node-id="727:1131">{`EGP 1,000 `}</p>
        <p className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[84px] not-italic text-[11px] text-[rgba(0,65,130,0.6)] text-left top-[178px] tracking-[-0.165px] whitespace-nowrap" data-node-id="727:1132">
          1,300 EGP
        </p>
        <div className="absolute h-0 left-[84px] top-[188px] w-[49px]" data-node-id="727:1133">
          <div className="absolute inset-[-1px_0_0_0]">
            <img alt="" className="block max-w-none size-full" src={imgLine1} />
          </div>
        </div>
        <div className="absolute inset-[calc(83.97%+1.36px)_calc(4.19%-1.83px)_calc(3.38%-1.86px)_calc(80.1%+1.2px)]" data-node-id="727:1134" data-name="🦆 icon 'plus circle'">
          <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgIconPlusCircle} />
        </div>
        <p className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[51px] not-italic text-[#06004f] text-[12px] text-left top-[204px] tracking-[-0.165px] whitespace-nowrap" data-node-id="727:1136">
          (4.8)
        </p>
        <div className="absolute inset-[calc(86.92%+1.48px)_calc(47.64%-0.09px)_calc(6.75%-1.73px)_calc(44.5%-0.22px)]" data-node-id="727:1137" data-name="🦆 emoji 'white medium star'">
          <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgEmojiWhiteMediumStar} />
        </div>
        <p className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[6px] not-italic text-[#06004f] text-[12px] text-left top-[204px] tracking-[-0.165px] whitespace-nowrap" data-node-id="727:1141">
          Review
        </p>
      </a>
      <div className="absolute border-2 border-[rgba(0,65,130,0.3)] border-solid h-[237px] left-[16px] overflow-clip rounded-[15px] top-[671px] w-[191px]" data-node-id="727:1170">
        <div className="absolute h-[128px] left-[-2px] overflow-clip rounded-tl-[15px] rounded-tr-[15px] top-[-2px] w-[191px]" data-node-id="727:1171">
          <img alt="" className="absolute inset-0 max-w-none object-cover pointer-events-none rounded-tl-[15px] rounded-tr-[15px] size-full" src={imgFrame70} />
          <Group17 className="absolute block cursor-pointer left-[153px] size-[30px] top-[8px]" />
        </div>
        <div className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[0] left-[6px] not-italic text-[#06004f] text-[14px] top-[134px] tracking-[-0.165px] whitespace-nowrap" data-node-id="727:1172">
          <p className="leading-[18px] mb-0">Nike Air Jordon</p>
          <p className="leading-[18px]">Nike shoes flexible for wo..</p>
        </div>
        <p className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[6px] not-italic text-[#06004f] text-[14px] top-[178px] tracking-[-0.165px] whitespace-nowrap" dir="auto" data-node-id="727:1173">{`EGP 1,800 `}</p>
        <p className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[84px] not-italic text-[11px] text-[rgba(0,65,130,0.6)] top-[178px] tracking-[-0.165px] whitespace-nowrap" data-node-id="727:1174">
          2,100 EGP
        </p>
        <div className="absolute h-0 left-[84px] top-[188px] w-[49px]" data-node-id="727:1175">
          <div className="absolute inset-[-1px_0_0_0]">
            <img alt="" className="block max-w-none size-full" src={imgLine1} />
          </div>
        </div>
        <div className="absolute inset-[calc(83.97%+1.36px)_calc(4.19%-1.83px)_calc(3.38%-1.86px)_calc(80.1%+1.2px)]" data-node-id="727:1176" data-name="🦆 icon 'plus circle'">
          <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgIconPlusCircle} />
        </div>
        <p className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[51px] not-italic text-[#06004f] text-[12px] top-[204px] tracking-[-0.165px] whitespace-nowrap" data-node-id="727:1178">
          (4.2)
        </p>
        <div className="absolute inset-[calc(86.92%+1.48px)_calc(47.64%-0.09px)_calc(6.75%-1.73px)_calc(44.5%-0.22px)]" data-node-id="727:1179" data-name="🦆 emoji 'white medium star'">
          <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgEmojiWhiteMediumStar} />
        </div>
        <p className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[6px] not-italic text-[#06004f] text-[12px] top-[204px] tracking-[-0.165px] whitespace-nowrap" data-node-id="727:1183">
          Review
        </p>
      </div>
      <div className="absolute border-2 border-[rgba(0,65,130,0.3)] border-solid h-[237px] left-[calc(50%+8px)] overflow-clip rounded-[15px] top-[418px] w-[191px]" data-node-id="727:1156">
        <div className="absolute h-[128px] left-[-2px] overflow-clip rounded-tl-[15px] rounded-tr-[15px] top-[-2px] w-[191px]" data-node-id="727:1157">
          <img alt="" className="absolute inset-0 max-w-none object-cover pointer-events-none rounded-tl-[15px] rounded-tr-[15px] size-full" src={imgFrame71} />
          <Group17 className="absolute left-[153px] size-[30px] top-[8px]" />
        </div>
        <div className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[0] left-[6px] not-italic text-[#06004f] text-[14px] top-[134px] tracking-[-0.165px] whitespace-nowrap" data-node-id="727:1158">
          <p className="leading-[18px] mb-0">Nike Air Jordon</p>
          <p className="leading-[18px]">Nike shoes flexible for wo..</p>
        </div>
        <p className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[6px] not-italic text-[#06004f] text-[14px] top-[178px] tracking-[-0.165px] whitespace-nowrap" dir="auto" data-node-id="727:1159">{`EGP 1,500 `}</p>
        <p className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[84px] not-italic text-[11px] text-[rgba(0,65,130,0.6)] top-[178px] tracking-[-0.165px] whitespace-nowrap" data-node-id="727:1160">
          2000 EGP
        </p>
        <div className="absolute h-0 left-[84px] top-[188px] w-[49px]" data-node-id="727:1161">
          <div className="absolute inset-[-1px_0_0_0]">
            <img alt="" className="block max-w-none size-full" src={imgLine1} />
          </div>
        </div>
        <div className="absolute inset-[calc(83.97%+1.36px)_calc(4.19%-1.83px)_calc(3.38%-1.86px)_calc(80.1%+1.2px)]" data-node-id="727:1162" data-name="🦆 icon 'plus circle'">
          <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgIconPlusCircle} />
        </div>
        <p className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[51px] not-italic text-[#06004f] text-[12px] top-[204px] tracking-[-0.165px] whitespace-nowrap" data-node-id="727:1164">
          (4.8)
        </p>
        <div className="absolute inset-[calc(86.92%+1.48px)_calc(47.64%-0.09px)_calc(6.75%-1.73px)_calc(44.5%-0.22px)]" data-node-id="727:1165" data-name="🦆 emoji 'white medium star'">
          <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgEmojiWhiteMediumStar} />
        </div>
        <p className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[6px] not-italic text-[#06004f] text-[12px] top-[204px] tracking-[-0.165px] whitespace-nowrap" data-node-id="727:1169">
          Review
        </p>
      </div>
      <div className="absolute border-2 border-[rgba(0,65,130,0.3)] border-solid h-[237px] left-[calc(50%+8px)] overflow-clip rounded-[15px] top-[671px] w-[191px]" data-node-id="727:1184">
        <div className="absolute h-[128px] left-[-2px] overflow-clip rounded-tl-[15px] rounded-tr-[15px] top-[-2px] w-[191px]" data-node-id="727:1185">
          <img alt="" className="absolute inset-0 max-w-none object-cover pointer-events-none rounded-tl-[15px] rounded-tr-[15px] size-full" src={imgFrame72} />
          <Group17 className="absolute block cursor-pointer left-[153px] size-[30px] top-[8px]" />
        </div>
        <div className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[0] left-[6px] not-italic text-[#06004f] text-[14px] top-[134px] tracking-[-0.165px] whitespace-nowrap" data-node-id="727:1186">
          <p className="leading-[18px] mb-0">Nike Air Jordon</p>
          <p className="leading-[18px]">Nike shoes flexible for wo..</p>
        </div>
        <p className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[6px] not-italic text-[#06004f] text-[14px] top-[178px] tracking-[-0.165px] whitespace-nowrap" dir="auto" data-node-id="727:1187">{`EGP 1,900 `}</p>
        <p className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[84px] not-italic text-[11px] text-[rgba(0,65,130,0.6)] top-[178px] tracking-[-0.165px] whitespace-nowrap" data-node-id="727:1188">
          2,500 EGP
        </p>
        <div className="absolute h-0 left-[84px] top-[188px] w-[49px]" data-node-id="727:1189">
          <div className="absolute inset-[-1px_0_0_0]">
            <img alt="" className="block max-w-none size-full" src={imgLine1} />
          </div>
        </div>
        <div className="absolute inset-[calc(83.97%+1.36px)_calc(4.19%-1.83px)_calc(3.38%-1.86px)_calc(80.1%+1.2px)]" data-node-id="727:1190" data-name="🦆 icon 'plus circle'">
          <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgIconPlusCircle} />
        </div>
        <p className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[51px] not-italic text-[#06004f] text-[12px] top-[204px] tracking-[-0.165px] whitespace-nowrap" data-node-id="727:1192">
          (3.8)
        </p>
        <div className="absolute inset-[calc(86.92%+1.48px)_calc(47.64%-0.09px)_calc(6.75%-1.73px)_calc(44.5%-0.22px)]" data-node-id="727:1193" data-name="🦆 emoji 'white medium star'">
          <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgEmojiWhiteMediumStar} />
        </div>
        <p className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[6px] not-italic text-[#06004f] text-[12px] top-[204px] tracking-[-0.165px] whitespace-nowrap" data-node-id="727:1197">
          Review
        </p>
      </div>
      <div className="absolute border-2 border-[rgba(0,65,130,0.3)] border-solid h-[237px] left-[calc(50%+8px)] overflow-clip rounded-[15px] top-[165px] w-[191px]" data-node-id="727:1114">
        <div className="absolute h-[128px] left-[-2px] overflow-clip rounded-tl-[15px] rounded-tr-[15px] top-[-2px] w-[191px]" data-node-id="727:1115">
          <img alt="" className="absolute inset-0 max-w-none object-cover pointer-events-none rounded-tl-[15px] rounded-tr-[15px] size-full" src={imgFrame73} />
          <Group17 className="absolute block cursor-pointer left-[153px] size-[30px] top-[8px]" />
        </div>
        <div className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[0] left-[6px] not-italic text-[#06004f] text-[14px] top-[134px] tracking-[-0.165px] whitespace-nowrap" data-node-id="727:1116">
          <p className="leading-[18px] mb-0">Nike Air Jordon</p>
          <p className="leading-[18px]">Nike shoes flexible for wo..</p>
        </div>
        <p className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[6px] not-italic text-[#06004f] text-[14px] top-[178px] tracking-[-0.165px] whitespace-nowrap" dir="auto" data-node-id="727:1117">{`EGP 1,100 `}</p>
        <p className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[84px] not-italic text-[11px] text-[rgba(0,65,130,0.6)] top-[178px] tracking-[-0.165px] whitespace-nowrap" data-node-id="727:1118">
          1,500 EGP
        </p>
        <div className="absolute h-0 left-[84px] top-[188px] w-[49px]" data-node-id="727:1119">
          <div className="absolute inset-[-1px_0_0_0]">
            <img alt="" className="block max-w-none size-full" src={imgLine2} />
          </div>
        </div>
        <div className="absolute inset-[calc(83.97%+1.36px)_calc(4.19%-1.83px)_calc(3.38%-1.86px)_calc(80.1%+1.2px)]" data-node-id="727:1120" data-name="🦆 icon 'plus circle'">
          <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgIconPlusCircle} />
        </div>
        <p className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[51px] not-italic text-[#06004f] text-[12px] top-[204px] tracking-[-0.165px] whitespace-nowrap" data-node-id="727:1122">
          (4.5)
        </p>
        <div className="absolute inset-[calc(86.92%+1.48px)_calc(47.64%-0.09px)_calc(6.75%-1.73px)_calc(44.5%-0.22px)]" data-node-id="727:1123" data-name="🦆 emoji 'white medium star'">
          <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgEmojiWhiteMediumStar} />
        </div>
        <p className="[word-break:break-word] absolute font-['Poppins:Regular'] leading-[18px] left-[6px] not-italic text-[#06004f] text-[12px] top-[204px] tracking-[-0.165px] whitespace-nowrap" data-node-id="727:1127">
          Review
        </p>
      </div>
      <div className="absolute bg-[#004182] h-[56px] left-0 overflow-clip rounded-[15px] top-[876px] w-[430px]" data-node-id="1008:450">
        <div className="absolute left-[148px] size-[40px] top-[8px]" data-node-id="I1008:450;964:523">
          <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgEllipse42} />
        </div>
        <a className="absolute block cursor-pointer left-[242px] size-[40px] top-[8px]" data-node-id="I1008:450;964:482">
          <div className="absolute left-[8px] size-[24px] top-[8px]" data-node-id="I1008:450;964:482;769:622" data-name="heart (3) 3">
            <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgHeart33} />
          </div>
        </a>
        <div className="absolute left-[148px] size-[40px] top-[8px]" data-node-id="I1008:450;964:483">
          <div className="absolute inset-[20%]" data-node-id="I1008:450;964:483;769:611" data-name="category (1) 3">
            <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgCategory13} />
          </div>
        </div>
        <div className="absolute left-[55px] size-[40px] top-[8px]" data-node-id="I1008:450;964:484">
          <div className="absolute left-[8px] size-[24px] top-[8px]" data-node-id="I1008:450;964:484;769:590" data-name="home (1) 3">
            <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgHome13} />
          </div>
        </div>
        <a className="absolute block cursor-pointer left-[335px] size-[40px] top-[8px]" data-node-id="I1008:450;964:485">
          <div className="absolute left-[8px] size-[24px] top-[8px]" data-node-id="I1008:450;964:485;769:633" data-name="user (1) 3">
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

These styles are contained in the design: description color: #06004F, white: #FFFFFF, text color: #06004F, general text: Font(family: "Poppins", style: Regular, size: 14, weight: 400, lineHeight: 18, letterSpacing: -0.16500000655651093), old price: Font(family: "Poppins", style: Regular, size: 11, weight: 400, lineHeight: 18, letterSpacing: -0.16500000655651093), main color: #004182, small text: Font(family: "Poppins", style: Regular, size: 12, weight: 400, lineHeight: 18, letterSpacing: -0.16500000655651093), strok color: #004182.

Images and SVGs will be stored as constants, e.g. const image = '../assets/550e8400-e29b-41d4-a716-446655440000.png'. These constants will be used in the code as the source for the image, ex: <img src={image} />. Image assets are stored on a remote server for 7 days and can be fetched using the provided URLs until they expire.
