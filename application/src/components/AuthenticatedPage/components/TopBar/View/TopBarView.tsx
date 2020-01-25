import React from "react";
import { Styled } from "../Styled";
import translator from "../../../../../config/i18next";
import {
  getDisplayName,
  getThumbnail
} from "../../../../../service/UserManager/UserManager";
import { Popover } from "antd";
import { ITopBar } from "../Container/ITopBar";

const TopBarView: React.FC<ITopBar> = ({ logout }) => {
  const content = (
    <Styled.LogoutButton onClick={logout} data-id="top-bar-logout-button">
      {translator.t("general.logoutButton")}
    </Styled.LogoutButton>
  );

  return (
    <Styled.TopBar>
      <Styled.TopBarTitle>
        {translator.t("general.applicationTitle")}
      </Styled.TopBarTitle>

      <Styled.UserName>{getDisplayName()}</Styled.UserName>

      <Popover content={content} trigger="hover">
        <Styled.UserImageContainer>
          <Styled.UserImage
            data-id="top-bar-profile-image"
            src={getThumbnail()}
          />
        </Styled.UserImageContainer>
      </Popover>
    </Styled.TopBar>
  );
};

export default TopBarView;
