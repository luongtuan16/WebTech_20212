import { SUCCESS_CODE } from "../../constraints";
import { userRequest } from "../../requestMethods";
import "./chatOnline.css";

export default function ChatOnline({ onUpdate, getMessages, setCurrentChat, onlineUsers, currentUser }) {
  const PF = process.env.REACT_APP_PUBLIC_FOLDER;
  const handleChat = async (partner) => {
    if (!currentUser)
      return;
    const res = await userRequest(currentUser?.token).get(`/make_conversation?partnerId=${partner.user_id}`);
    if (res.data.code == SUCCESS_CODE) {
      setCurrentChat({
        id: res.data.conversationId,
        partner: {
          id: partner.user_id,
          avatar: partner.avatar,
          username: partner.user_name
        },
      });

      getMessages(partner.user_id);
      onUpdate();
    }
  }
  return (
    <div className="chatOnline">
      {onlineUsers.map((o, idx) => (
        <div key={idx} className="chatOnlineFriend" onClick={() => handleChat(o)}>
          <div className="chatOnlineImgContainer">
            <img
              className="chatOnlineImg"
              src={
                o?.avatar
                  ? PF + o.avatar.substring(o.avatar.lastIndexOf("\\") + 1, o.avatar.length)
                  : PF + "default_user.png"
              }
              alt=""
            />
            <div className="chatOnlineBadge"></div>
          </div>
          <span className="chatOnlineName">{o?.user_name}</span>
        </div>
      ))}
    </div>
  );
}
