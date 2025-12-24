import ProfileContent from "@/app/components/ProfileContent";
import BottomNav from "@/app/components/BottomNav";

export default function ProfilePage() {
  return (
    <main className="min-h-screen bg-white md:max-w-[390px] md:mx-auto md:shadow-xl">
      <ProfileContent />
      <BottomNav />
    </main>
  );
}
