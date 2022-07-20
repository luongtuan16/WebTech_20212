import "./conversation.css";

export default function Conversation({ conversation, currentUser, lastMessage }) {
  //console.log(lastMessage);
  const PF = process.env.REACT_APP_PUBLIC_FOLDER;
  return (
    <div className="conversation">
      <img
        className="conversationImg"
        src={
          conversation?.partner?.avatar
            ? PF + conversation?.partner.avatar.substring(conversation?.partner.avatar.lastIndexOf("\\") + 1, conversation?.partner.avatar.length)
            : PF + "default_user.png"
        }
        alt=""
      />
      <div className="conversation-right">

        <span className="conversationName">{conversation?.partner?.username}</span>
        <span>{lastMessage ? lastMessage.content : conversation?.lastMessage?.message}</span>
      </div>
    </div>
  );
}
