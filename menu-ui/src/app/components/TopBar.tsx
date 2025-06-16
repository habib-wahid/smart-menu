
import Image from 'next/image'
import profileImage from '../../../public/profileImage.svg'
import accountDetails from '../../../public/account-details.svg'

export default function TopBar() {
    return (
        <div className="flex justify-between items-center m-5">
            <h2 className="text-3xl font-bold">Menu</h2>
            <div className="flex items-center gap-4">
              <Image src={profileImage} alt="Profile Image" className="w-10 h-10 rounded-full" />
              <Image src={accountDetails} alt="Profile Image"/>
            </div>
          </div>
        
    )
}