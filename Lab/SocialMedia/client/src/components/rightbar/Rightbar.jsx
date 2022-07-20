import { Add, Remove } from "@material-ui/icons";
import { useContext, useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { SUCCESS_CODE } from "../../constraints";
import { AuthContext } from "../../context/AuthContext";
import { userRequest } from "../../requestMethods";
import EditUser from "../editUser/EditUser";
import Online from "../online/Online";
import "./rightbar.css";

export default function Rightbar({ user, onUpdate }) {
  //console.log(user)
  const PF = process.env.REACT_APP_PUBLIC_FOLDER;
  const [friends, setFriends] = useState(null);
  const [requests, setRequests] = useState([]);
  const { user: currentUser, dispatch } = useContext(AuthContext);
  const [isFriend, setIsFriend] = useState(false);
  const [sentReq, setSentReq] = useState(-1);//0: sent request, 1: received request
  //const [update]
  //console.log(sentReq)
  useEffect(() => {
    const getFriends = async () => {
      const users = await userRequest(currentUser?.token)
        .get(`/get_user_friends`, {
          params: {
            index: 0,
            count: 50,
            user_id: user ? user.id : null
          }
        });
      if (users.data.code == SUCCESS_CODE) {
        setFriends(users.data.data?.friends);
        setIsFriend(users.data.data?.friends.find(friend => friend.user_id == currentUser.id) ? true : false)
      }
    }
    getFriends();
  }, [user]);

  useEffect(() => {
    const getRequests = async () => {
      const users = await userRequest(currentUser?.token)
        .post(`/get_requested_friend`, {
          index: 0,
          count: 50,
          user_id: currentUser.id
        });
      if (users.data.code == SUCCESS_CODE) {
        setRequests(users.data.data?.friends);
        if (user)
          setSentReq(users.data.data?.friends.find(friend => friend.id == user.id) ? 1 : -1);
      }
    }
    getRequests();
  }, [user]);

  const handleClick = async () => {
    try {
      if (!user.id)
        return;
      if (!isFriend) {
        if (sentReq === -1) {//add friend
          const res = await userRequest(currentUser.token).get(`/set_request_friend?user_id=${user.id}`);
          setSentReq(0);

        } else if (sentReq === 1) {//accept friend
          const res = await userRequest(currentUser.token).get(`/set_accept_friend?user_id=${user.id}&is_accept=${1}`);
          if (res.data.code == SUCCESS_CODE) {
            setIsFriend(true);
            setSentReq(-1);
          } else alert("Accept request fail");
        }
      } else {//unfriend
        const res = await userRequest(currentUser.token).get(`/set_accept_friend?user_id=${user.id}&is_accept=${0}`);
        if (res.data.code == SUCCESS_CODE) {
          setIsFriend(false);
          setSentReq(-1);
        } else alert("Unfriend fail");
      }
    } catch (err) {
      console.log(err)
    }
  };
  const handleAccept = async (id) => {
    console.log(id)
    const res = await userRequest(currentUser.token).get(`/set_accept_friend?user_id=${id}&is_accept=${1}`);
    if (res.data.code == SUCCESS_CODE) {
      onUpdate();
    } else alert("Accept request fail");
  }
  const HomeRightbar = () => {
    //console.log(friends)
    return (
      <>
        {/* <img className="rightbarAd" src="assets/ad.png" alt="" /> */}
        <h4 className="rightbarTitle">Bạn bè đang hoạt động</h4>
        <ul className="rightbarFriendList">
          {friends ? friends.map((u) => (
            <Online key={u.user_id} user={u} />
          )) : <></>}
        </ul>
      </>
    );
  };

  const renderStateButton = () => {
    if (isFriend)
      return <button className="rightbarFollowButton" onClick={handleClick}>
        UnFriend <Remove />
      </button>
    else if (sentReq === 1)
      return <button className="rightbarFollowButton" onClick={handleClick}>
        Accept Request <Add />
      </button>
    else if (sentReq === 0)
      return <button className="rightbarFollowButton" disabled>
        Sent Request
      </button>
    else if (sentReq === -1)
      return <button className="rightbarFollowButton" onClick={handleClick}>
        Add Friend <Add />
      </button>
  }
  const ProfileRightbar = () => {
    return (
      <>
        {user.id != currentUser.id ? renderStateButton() : <EditUser onUpdate={onUpdate} user={user} token={currentUser.token} />}
        <h4 className="rightbarTitle">USER INFORMATION</h4>
        <div className="rightbarInfo">
          <div className="rightbarInfoItem">
            <span className="rightbarInfoKey">City:</span>
            <span className="rightbarInfoValue">{user.city}</span>
          </div>
          <div className="rightbarInfoItem">
            <span className="rightbarInfoKey">From:</span>
            <span className="rightbarInfoValue">{user.address}</span>
          </div>
          <div className="rightbarInfoItem">
            <span className="rightbarInfoKey">Description:</span>
            <span className="rightbarInfoValue">
              {user.description}
            </span>
          </div>
        </div>
        <h4 className="rightbarTitle">User friends</h4>
        <div className="rightbarFollowings">
          {friends ? friends.map((friend) => (
            <Link key={friend.user_id}
              to={"/profile/" + friend.user_id}
              style={{ textDecoration: "none" }}
            >
              <div className="rightbarFollowing">
                <img
                  src={
                    friend.avatar
                      ? PF + friend.avatar.substring(friend.avatar.lastIndexOf("\\") + 1, friend.avatar.length)
                      : PF + "default_user.png"
                  }
                  alt=""
                  className="rightbarFollowingImg"
                />
                <span className="rightbarFollowingName">{friend.user_name}</span>
              </div>
            </Link>
          )) : <></>}
        </div>

        {user.id == currentUser.id && <><h4 className="rightbarTitle">Add Friend Requests</h4>
          <div className="rightbarFollowings">
            {requests ? requests.map((friend) => (
              <div key={friend.id} className="rightbarFollowing">
                <Link
                  to={"/profile/" + friend.id}
                  style={{ textDecoration: "none" }}
                >
                  <img
                    src={
                      friend.avatar
                        ? PF + friend.avatar.substring(friend.avatar.lastIndexOf("\\") + 1, friend.avatar.length)
                        : PF + "default_user.png"
                    }
                    alt=""
                    className="rightbarFollowingImg"
                  />
                </Link>
                <span style={{ marginTop: "10px" }} className="rightbarFollowingName">{friend.username}</span>
                <button className="rightbarFollowButton" onClick={() => handleAccept(friend.id)}>Accept</button>
              </div>
            )) : <></>}
          </div></>}
      </>
    );
  };
  return (
    <div className="rightbar">
      <div className="rightbarWrapper">
        {user ? <ProfileRightbar /> : <HomeRightbar />}
      </div>
    </div>
  );
}
