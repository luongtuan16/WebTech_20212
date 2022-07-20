import {
  Bookmark, Chat, Event, Group, HelpOutline, PlayCircleFilledOutlined, RssFeed, WorkOutline
} from "@material-ui/icons";
import { useContext, useEffect, useState } from "react";
import { Link } from 'react-router-dom';
import { SUCCESS_CODE } from "../../constraints";
import { AuthContext } from '../../context/AuthContext';
import { publicRequest, userRequest } from "../../requestMethods";
import LinktoFriend from "../LinktoFriend";
import "./sidebar.css";
export default function Sidebar() {
  const {user} = useContext(AuthContext);
  const [Users, setUsers] = useState(null);
  useEffect(() => {
    const getFriends = async () => {
      const users = await userRequest(user?.token)
        .get(`/get_user_friends`, {
          params: {
            //token: user?.token,
            index: 0,
            count: 50
          }
        });
      if (users.data.code == SUCCESS_CODE) {
        setUsers(users.data.data?.friends);
      }
    }
    getFriends();
    //console.log(Users)
  }, [])
  return (
    <div className="sidebar">
      <div className="sidebarWrapper">
        <ul className="sidebarList">
          <li className="sidebarListItem">
            <RssFeed className="sidebarIcon" />
            <span className="sidebarListItemText">Feed</span>
          </li>
          <Link to={`/messenger`}>
            <li className="sidebarListItem">
              <Chat className="sidebarIcon" />
              <span className="sidebarListItemText">Chat</span>
            </li>
          </Link>

          <li className="sidebarListItem">
            <PlayCircleFilledOutlined className="sidebarIcon" />
            <span className="sidebarListItemText">Videos</span>
          </li>
          <li className="sidebarListItem">
            <Group className="sidebarIcon" />
            <span className="sidebarListItemText">Nhóm của bạn</span>
          </li>
          <li className="sidebarListItem">
            <Bookmark className="sidebarIcon" />
            <span className="sidebarListItemText">Đã lưu</span>
          </li>
          <li className="sidebarListItem">
            <HelpOutline className="sidebarIcon" />
            <span className="sidebarListItemText">Câu hỏi</span>
          </li>
          <li className="sidebarListItem">
            <WorkOutline className="sidebarIcon" />
            <span className="sidebarListItemText">Công việc</span>
          </li>
          <li className="sidebarListItem">
            <Event className="sidebarIcon" />
            <span className="sidebarListItemText">Sự kiện</span>
          </li>

        </ul>
        <button className="sidebarButton">Xem thêm</button>
        <hr className="sidebarHr" />
        <ul className="sidebarFriendList">
          {Users ? Users.map((u, idx) => (
            <LinktoFriend key={idx} u={u}></LinktoFriend>
          )) : <></>}
        </ul>
      </div>
    </div>
  );
}
