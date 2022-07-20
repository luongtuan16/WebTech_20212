import axios from "axios";
import { useContext, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { SUCCESS_CODE } from "../../constraints";
import { AuthContext } from "../../context/AuthContext";
import { publicRequest, userRequest } from "../../requestMethods";
import Post from "../post/Post";
import Share from "../share/Share";
import "./feed.css";

export default function Feed({ userId }) {
  const [posts, setPosts] = useState([]);
  const { user } = useContext(AuthContext);
  const [update, setUpdate] = useState(false);
  const id = useParams().id;
  useEffect(() => {
    const fetchPosts = async () => {
      //console.log(userId);
      const res = await userRequest(user?.token).post("/get_list_posts", {
        index: 0,
        count: 50,
        userId: id,
      });
      if (res.data.code == SUCCESS_CODE) {
        //console.log(res.data.data.posts);
        setPosts(
          res.data.data.posts.sort((p1, p2) => {
            return new Date(p2.created) - new Date(p1.created);
          })
        );
      }
    };
    fetchPosts();
  }, [userId, user, update]);

  //console.log(posts);
  return (
    <div className="feed">
      <div className="feedWrapper">
        {(!id || id === user.id) &&
          <Share onUpdate={() => setUpdate(!update)} />}
        {posts.map((p) => (
          <Post onUpdate={() => setUpdate(!update)} key={p.id} apost={p} />
        ))}
      </div>
    </div>
  );
}
