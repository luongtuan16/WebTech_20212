import { useContext, useEffect, useRef, useState } from "react";
import { io } from "socket.io-client";
import ChatOnline from "../../components/chatOnline/ChatOnline";
import Conversation from "../../components/conversations/Conversation";
import Message from "../../components/message/Message";
import Topbar from "../../components/topbar/Topbar";
import { SUCCESS_CODE } from "../../constraints";
import { AuthContext } from "../../context/AuthContext";
import { userRequest } from "../../requestMethods";
import "./messenger.css";

export default function Messenger() {
  const [conversations, setConversations] = useState([]);
  const [currentChat, setCurrentChat] = useState(null);
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState("");
  const [arrivalMessage, setArrivalMessage] = useState(null);
  const [lastMessage, setLastMessage] = useState({});
  const [onlineUsers, setOnlineUsers] = useState([]);
  const [update, setUpdate] = useState(false);
  const socket = useRef();
  const { user } = useContext(AuthContext);
  const scrollRef = useRef();

  useEffect(() => {
    socket.current = io("ws://localhost:8900");
  }, []);

  useEffect(() => {
    if (arrivalMessage && currentChat?.partner?.id == arrivalMessage.sender) {
      const sender = onlineUsers.find(user => user.user_id == arrivalMessage.sender);
      if (!sender)
        return;

      setMessages(prev => [...prev, {
        message: arrivalMessage.text,
        created: arrivalMessage.created,
        sender: {
          id: sender.user_id,
          avatar: sender.avatar,
          username: sender.user_name
        }
      }]);
      setLastMessage({
        partnerId: sender.user_id,
        content: arrivalMessage.text
      });
      setArrivalMessage('');
    }
  }, [arrivalMessage]);
  useEffect(() => {
    if (!socket.current || !user.id)
      return;
    socket.current.emit("addUser", user.id);
  }, [user, socket.current]);

  useEffect(() => {//get - set list conversations
    const getConversations = async () => {
      try {
        const res = await userRequest(user.token).get("/get_list_conversation", {
          params: {
            index: 0,
            count: 10000
          }
        });
        if (res.data.code == SUCCESS_CODE) {
          setConversations(res.data.data);
        } else setConversations([]);
      } catch (err) {
        console.log(err);
      }
    };
    getConversations();
  }, [user.id, update]);

  const getMessages = async (partnerId) => {//get - set list messages of current conversation
    if (!partnerId)
      return;
    try {
      const res = await userRequest(user.token).post("/get_conversation", {
        index: 0,
        count: 1000,
        partner_id: partnerId
      });
      if (res.data.code == SUCCESS_CODE) {
        //console.log(res.data.data.conversation);
        setMessages(res.data.data?.conversation?.reverse());
      }
      else alert(res.data.message);
    } catch (err) {
      console.log(err);
    }
  };
  useEffect(() => {//set list messages of current conversation
    getMessages(currentChat?.partner?.id);
  }, [currentChat]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    //console.log(currentChat);
    if (!newMessage)
      return;
    const message = {
      partnerId: currentChat.partner.id,
      content: newMessage,
      conversationId: currentChat.id,
    };

    socket.current.emit("sendMessage", {
      senderId: user.id,
      receiverId: currentChat?.partner.id,
      text: newMessage,
    });

    try {
      const res = await userRequest(user.token).post("/create_message", message);
      if (res.data.code == SUCCESS_CODE) {
        setMessages([...messages, {
          message: message.content,
          created: Date.now(),
          sender: {
            id: user.id,
            avatar: user.avatar,
            username: user.username
          }
        }]);
        setLastMessage({
          partnerId: message.partnerId,
          content: message.content
        });
        setNewMessage("");
      }
    } catch (err) {
      console.log(err);
    }
  };
  //console.log(currentChat);
  useEffect(() => {
    scrollRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  useEffect(() => {
    if (!socket.current)
      return;
    socket.current.on("getMessage", (data) => {
      setArrivalMessage({
        sender: data.senderId,
        text: data.text,
        created: Date.now(),
      });
    });
    socket.current.on("getUsers", async (users) => {
      const us = await userRequest(user?.token)
        .get(`/get_user_friends`, {
          params: {
            index: 0,
            count: 50,
            user_id: user.id
          }
        });
      if (us.data.code == SUCCESS_CODE) {
        setOnlineUsers(
          us.data.data.friends.filter((f) => users.some((u) => u.userId == f.user_id))
        );
      }
    });
  }, [socket.current])
  return (
    <>
      <Topbar />
      <div className="messenger">
        <div className="chatMenu">
          <div className="chatMenuWrapper">
            <input placeholder="Search for friends" className="chatMenuInput" />
            {conversations.map((c) => (
              <div onClick={() => setCurrentChat(c)}>
                <Conversation
                  conversation={c} currentUser={user}
                  lastMessage={c?.partner?.id == lastMessage?.partnerId && lastMessage} />
              </div>
            ))}
          </div>
        </div>
        <div className="chatBox">
          <div className="chatBoxWrapper">
            {currentChat ? (
              <>
                <div className="chatBoxTop">
                  {messages.map((m, idx) => (
                    <div key={idx} ref={scrollRef}>
                      <Message message={m} own={m.sender.id == user.id} />
                    </div>
                  ))}
                </div>
                <form className="chatBoxBottom">
                  <input
                    className="chatMessageInput"
                    placeholder="write message..."
                    onChange={(e) => setNewMessage(e.target.value)}
                    value={newMessage}
                  ></input>
                  <button type="submit" className="chatSubmitButton" onClick={handleSubmit}>
                    Send
                  </button>
                </form>
              </>
            ) : (
              <span className="noConversationText">
                Open a conversation to start a chat.
              </span>
            )}
          </div>
        </div>
        <div className="chatOnline">
          <div className="chatOnlineWrapper">
            <ChatOnline
              onlineUsers={onlineUsers}
              currentUser={user}
              setCurrentChat={setCurrentChat}
              getMessages={getMessages}
              onUpdate={() => setUpdate(!update)}
            />
          </div>
        </div>
      </div>
    </>
  );
}
