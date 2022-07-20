import "./topbar.css";
import { Search, Person, Chat, Notifications } from "@material-ui/icons";
import { Link } from "react-router-dom";
import { useContext } from "react";
import { Avatar } from '@material-ui/core'
import { AuthContext } from "../../context/AuthContext";
import { useEffect, useState } from "react";
import { logoutCall } from './../../apiCalls'
import axios from 'axios'
const BASE_URL = "http://localhost:8800/api/users/all"
export default function Topbar() {
  const [alluser, setAllUser] = useState(null);
  const [searchuser, setSearchUser] = useState(null);
  const { user, dispatch } = useContext(AuthContext);
  const [visible, setVisible] = useState(false);
  
  const handleLogout = (e) => {

    logoutCall(dispatch);
  }
  // useEffect(() => {
  //   try {
  //     const getUser = async () => {
  //       await axios.get(BASE_URL).then((res) => {
  //         setAllUser(res.data);
  //       });
  //     }
  //     getUser();
  //   }
  //   catch (err) {
  //     console.log(err);
  //   }
  // }, []);

  const onChange = (e) => {
    if (e.target.value) {
      setVisible(true)
      setSearchUser(alluser.filter((user) => {
        return (user.username.indexOf(e.target.value) + 1)
      }));
      console.log(searchuser)
    }
    else setSearchUser(null);

  }

  const PF = process.env.REACT_APP_PUBLIC_FOLDER;

  return (
    <div className="topbarContainer">
      <div className="topbarLeft">
        <Link to="/" style={{ textDecoration: "none" }}>
          <span className="logo">Midusocial</span>
        </Link>
      </div>
      <div className="topbarCenter">
        <div className="searchbar">
          <Search className="searchIcon" />
          <input
            placeholder="Tìm kiếm bạn bè, người dùng"
            className="searchInput"
            onChange={onChange}

            onBlur={() => { setSearchUser(null) }}
          />
          <ul style={{ listStyle: 'none', visibility: { visible } }} className="dropdown">
            {searchuser ? searchuser.map(user => { return <Link to={`/profile/${user.id}`} className="user-search" style={{ display: 'flex', lineHeight: '50px' }}><Avatar style={{ right: '30px' }} src={PF + user.profilePicture}></Avatar><div style={{ lineHeight: '50px' }}>{user.username}</div></Link> }) : <></>}
          </ul>

        </div>
      </div>
      <div className="topbarRight">
        <Link to="/">
          <div className="topbarLinks">
            <span className="topbarLink">Trang chủ</span>

          </div>
        </Link>

        <div className="topbarIcons">

          <Link to={`/profile/${user.id}`}>
            <div className="topbarIconItem">
              <Person />
              {/* <span className="topbarIconBadge">1</span> */}
            </div>
          </Link>

          <Link to={`/messenger`}>
            <div className="topbarIconItem">
              <Chat />
              <span className="topbarIconBadge">2</span>
            </div></Link>


          <div className="topbarIconItem">
            <Notifications />
            <span className="topbarIconBadge">1</span>
          </div>
          <Link to={`/`} className="topbarIconItem" onClick={handleLogout}>Đăng xuất</Link>
        </div>
        <Link to={`/profile/${user.id}`}>
          <img
            src={
              user.avatar
                ? PF + user.avatar.substring(user.avatar.lastIndexOf("\\") + 1, user.avatar.length)
                : PF + "default_user.png"
            }
            alt=""
            className="topbarImg"
          />
        </Link>
      </div>
    </div>
  );
}
