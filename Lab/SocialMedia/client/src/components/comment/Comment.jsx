import React from 'react'
import { useState } from 'react'
import { ListItem, Avatar, Popover, Typography } from '@material-ui/core'
import { MoreVert } from '@material-ui/icons'
import { useContext } from 'react';
import { AuthContext } from "../../context/AuthContext";
import axios from 'axios';
import { userRequest } from '../../requestMethods';
import { SUCCESS_CODE } from '../../constraints';

export default function Comment({ postId, comment, message, callComment }) {

  const PF = process.env.REACT_APP_PUBLIC_FOLDER;
  const { user: currentUser } = useContext(AuthContext);
  const [visible, setVisible] = useState(false)
  const handleClose = () => {
    setVisible(null);
  }
  const onhandleDelete = async () => {
    try {
      const delComment = await userRequest(currentUser.token).get('/del_comment', {
        params: {
          id: postId,
          id_com: comment.id,
        }
      })
      if (delComment.data.code == SUCCESS_CODE)
        callComment();
      else alert('Del Comment Failed');
    }
    catch (err) {
      console.log(err)
    }
  }

  const onhandleChange = async () => {
    if (!message || message.length === 0)
      return;
    //console.log(comment)
    try {
      const editComment = await userRequest(currentUser.token).get('/edit_comment', {
        params: {
          id: postId,
          id_com: comment.id,
          comment: message
        }
      })
      if (editComment.data.code == SUCCESS_CODE)
        callComment();
      else alert('Edit Comment Failed');
    }
    catch (err) {
      console.log(err)
    }
    //callComment();
  }
  const onhandleClick = (e) => {
    //console.log(visible)
    if (comment.userId === currentUser._id)
      setVisible(e.target);
    //console.log(comment)
  }
  const open = Boolean(visible);
  const id = open ? 'simple-popper' : undefined;
  return (
    <ListItem style={{ flexWrap: 'wrap' }}>
      <div style={{ display: 'flex', alignItem: 'center', width: '500px' }}><Avatar src={PF + comment?.poster?.avatar.substring(comment?.poster?.avatar.lastIndexOf("\\") + 1, comment?.poster?.avatar.length)}></Avatar>
        <div style={{ padding: '10px ' }}> {comment.username}</div>
        <MoreVert style={{ marginLeft: '300px' }} onClick={onhandleClick}></MoreVert>
        <Popover id={id} open={open} onClose={handleClose} anchorEl={visible} transformOrigin={{
          vertical: 'top',
          horizontal: 'left',
        }} anchorOrigin={{
          vertical: 'top',
          horizontal: 'left',
        }}>
          <Typography sx={{ p: 20 }} style={{ cursor: 'pointer' }} onClick={onhandleDelete}>Xóa</Typography>
          <Typography sx={{ p: 20 }} style={{ cursor: 'pointer' }} onClick={onhandleChange}>Sửa</Typography>

        </Popover></div>
      <div style={{ margin: '10px' }}>{comment.content}</div>
    </ListItem>
  )
}
