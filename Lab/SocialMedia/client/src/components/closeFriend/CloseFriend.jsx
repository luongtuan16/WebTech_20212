import "./closeFriend.css";

export default function CloseFriend({ user }) {
  const PF = process.env.REACT_APP_PUBLIC_FOLDER;
  return (
    <li className="sidebarFriend">
      <img className="sidebarFriendImg" src={PF + user.avatar.substring(user.avatar.lastIndexOf("\\") + 1, user.avatar.length)} alt="" />
      <span className="sidebarFriendName">{user.user_name}</span>
    </li>
  );
}
