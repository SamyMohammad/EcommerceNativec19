const imgRectangle = "../assets/15673e39-155d-47c0-b24f-92f1514e1aa4.svg";
const imgCombinedShape = "../assets/e69deb4d-92aa-463a-94bd-4b0901b581c6.svg";
const imgWiFi = "../assets/23689b01-8fcf-493e-a723-ebf8448b87e4.svg";
const imgGroup5 = "../assets/e5f42c50-7201-4a8d-85be-66a15fb2aa7b.svg";
const imgEdit11 = "../assets/796bff72-403a-4a5d-8fe3-8e445315cf6e.svg";
const imgEllipse40 = "../assets/ed2b6b5b-a461-4042-bca3-dc8445e2ed02.svg";
const imgHeart33 = "../assets/d0166f2a-6c9d-4177-9d94-91bf08902ce9.svg";
const imgCategory13 = "../assets/df8319b6-2fd0-4bf3-a614-ae6915839914.svg";
const imgHome13 = "../assets/8510d5e7-dce0-4896-ab8c-02236c66a4be.svg";
const imgUser13 = "../assets/b15673e0-8096-4b88-9251-75af8ec9bb42.svg";

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

export default function Account() {
  return (
    <div className="bg-white overflow-clip relative rounded-[20px] size-full" data-node-id="835:523" data-name="Account">
      <StatusBar className="absolute bg-white h-[44px] left-px right-px rounded-[20px] top-0" />
      <div className="absolute h-[22px] left-[16px] top-[50px] w-[66px]" data-node-id="835:525">
        <div className="absolute inset-[-3.41%_-1.14%_-5.64%_-1.14%]">
          <img alt="" className="block max-w-none size-full" src={imgGroup5} />
        </div>
      </div>
      <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[109px] not-italic text-[#06004f] text-[18px] text-center top-[96px] tracking-[-0.165px] whitespace-nowrap" data-node-id="881:756">
        Welcome, Mohamed
      </p>
      <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[104px] not-italic text-[14px] text-[rgba(6,0,79,0.6)] text-center top-[122px] tracking-[-0.165px] whitespace-nowrap" data-node-id="881:758">
        mohamed.N@gmail.com
      </p>
      <div className="absolute border border-[rgba(0,65,130,0.3)] border-solid h-[54px] left-[16px] overflow-clip rounded-[15px] top-[550px] w-[398px]" data-node-id="882:759">
        <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[54.5px] not-italic text-[#06004f] text-[14px] text-center top-[17px] tracking-[-0.165px] whitespace-nowrap" data-node-id="882:761">
          01122118855
        </p>
        <div className="absolute left-[357px] size-[24px] top-[14px]" data-node-id="884:764" data-name="edit (1) 1">
          <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgEdit11} />
        </div>
      </div>
      <div className="absolute border border-[rgba(0,65,130,0.3)] border-solid h-[54px] left-[16px] overflow-clip rounded-[15px] top-[662px] w-[398px]" data-node-id="884:766">
        <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[94px] not-italic text-[#06004f] text-[14px] text-center top-[17px] tracking-[-0.165px] whitespace-nowrap" data-node-id="884:768">
          6th October, street 11.....
        </p>
        <div className="absolute left-[357px] size-[24px] top-[14px]" data-node-id="884:771" data-name="edit (1) 2">
          <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgEdit11} />
        </div>
      </div>
      <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[75.5px] not-italic text-[#06004f] text-[18px] text-center top-[628px] tracking-[-0.165px] whitespace-nowrap" dir="auto" data-node-id="884:767">
        Your Address
      </p>
      <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[107.5px] not-italic text-[#06004f] text-[18px] text-center top-[516px] tracking-[-0.165px] whitespace-nowrap" dir="auto" data-node-id="882:760">
        Your mobile number
      </p>
      <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[82.5px] not-italic text-[#06004f] text-[18px] text-center top-[404px] tracking-[-0.165px] whitespace-nowrap" data-node-id="885:445">
        Your password
      </p>
      <div className="absolute border border-[rgba(0,65,130,0.3)] border-solid h-[54px] left-[16px] overflow-clip rounded-[15px] top-[438px] w-[398px]" data-node-id="885:446">
        <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[91px] not-italic text-[#06004f] text-[14px] text-center top-[17px] tracking-[-0.165px] whitespace-nowrap" data-node-id="885:447">
          **********************
        </p>
        <div className="absolute left-[357px] size-[24px] top-[14px]" data-node-id="885:450" data-name="edit (1) 5">
          <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgEdit11} />
        </div>
      </div>
      <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[68.5px] not-italic text-[#06004f] text-[18px] text-center top-[292px] tracking-[-0.165px] whitespace-nowrap" data-node-id="884:774">
        Your E-mail
      </p>
      <div className="absolute border border-[rgba(0,65,130,0.3)] border-solid h-[54px] left-[16px] overflow-clip rounded-[15px] top-[326px] w-[398px]" data-node-id="884:775">
        <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[103px] not-italic text-[#06004f] text-[14px] text-center top-[17px] tracking-[-0.165px] whitespace-nowrap" data-node-id="884:776">
          mohamed.N@gmail.com
        </p>
        <div className="absolute left-[357px] size-[24px] top-[14px]" data-node-id="884:779" data-name="edit (1) 3">
          <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgEdit11} />
        </div>
      </div>
      <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[81px] not-italic text-[#06004f] text-[18px] text-center top-[180px] tracking-[-0.165px] whitespace-nowrap" data-node-id="884:781">
        Your full name
      </p>
      <div className="absolute border border-[rgba(0,65,130,0.3)] border-solid h-[54px] left-[16px] overflow-clip rounded-[15px] top-[214px] w-[398px]" data-node-id="884:783">
        <p className="-translate-x-1/2 [word-break:break-word] absolute font-['Poppins:Medium'] leading-[18px] left-[108px] not-italic text-[#06004f] text-[14px] text-center top-[17px] tracking-[-0.165px] whitespace-nowrap" data-node-id="884:784">
          Mohamed Mohamed Nabil
        </p>
        <div className="absolute left-[357px] size-[24px] top-[14px]" data-node-id="884:787" data-name="edit (1) 4">
          <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgEdit11} />
        </div>
      </div>
      <div className="absolute bg-[#004182] h-[56px] left-0 overflow-clip rounded-[15px] top-[876px] w-[430px]" data-node-id="1009:478">
        <div className="absolute left-[335px] size-[40px] top-[8px]" data-node-id="I1009:478;964:521">
          <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgEllipse40} />
        </div>
        <a className="absolute block cursor-pointer left-[242px] size-[40px] top-[8px]" data-node-id="I1009:478;964:508">
          <div className="absolute left-[8px] size-[24px] top-[8px]" data-node-id="I1009:478;964:508;769:622" data-name="heart (3) 3">
            <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgHeart33} />
          </div>
        </a>
        <a className="absolute block cursor-pointer left-[148px] size-[40px] top-[8px]" data-node-id="I1009:478;964:509">
          <div className="absolute inset-[20%]" data-node-id="I1009:478;964:509;769:611" data-name="category (1) 3">
            <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgCategory13} />
          </div>
        </a>
        <div className="absolute left-[55px] size-[40px] top-[8px]" data-node-id="I1009:478;964:510">
          <div className="absolute left-[8px] size-[24px] top-[8px]" data-node-id="I1009:478;964:510;769:590" data-name="home (1) 3">
            <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgHome13} />
          </div>
        </div>
        <div className="absolute left-[335px] size-[40px] top-[8px]" data-node-id="I1009:478;964:511">
          <div className="absolute left-[8px] size-[24px] top-[8px]" data-node-id="I1009:478;964:511;769:633" data-name="user (1) 3">
            <img alt="" className="absolute block inset-0 max-w-none size-full" src={imgUser13} />
          </div>
        </div>
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

These styles are contained in the design: text color: #06004F, header 1: Font(family: "Poppins", style: Medium, size: 18, weight: 500, lineHeight: 18, letterSpacing: -0.16500000655651093), description color: #06004F, sub-category: Font(family: "Poppins", style: Medium, size: 14, weight: 500, lineHeight: 18, letterSpacing: -0.16500000655651093), strok color: #004182, white: #FFFFFF, main color: #004182.

Images and SVGs will be stored as constants, e.g. const image = '../assets/550e8400-e29b-41d4-a716-446655440000.png'. These constants will be used in the code as the source for the image, ex: <img src={image} />. Image assets are stored on a remote server for 7 days and can be fetched using the provided URLs until they expire.
