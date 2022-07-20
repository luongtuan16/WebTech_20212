import "./message.css";
import { format } from "timeago.js";

export default function Message({message, own }) {
  const PF = process.env.REACT_APP_PUBLIC_FOLDER;
  return (
    <div className={own ? "message own" : "message"}>
      <div className="messageTop">
        <img
          className="messageImg"
          src={
            message.sender?.avatar
            ? PF + message.sender.avatar.substring(message.sender.avatar.lastIndexOf("\\") + 1, message.sender.avatar.length)
            : PF + "default_user.png"
          }
          alt=""
        />
        <p className="messageText">{message.message}</p>
      </div>
      <div className="messageBottom">{format(message.created)}</div>
    </div>
  );
}
