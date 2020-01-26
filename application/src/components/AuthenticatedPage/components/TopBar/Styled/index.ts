import { TopBar } from "./TopBar";
import { TopBarTitle } from "./TopBarTitle";
import { UserImageContainer } from "./UserImageContainer";
import { UserImage } from "./UserImage";
import { UserName } from "./UserName";
import { ProfileButton } from "./ProfileButton";
import styled from "styled-components";

const ProfileButtons = styled.div`
  display: flex;
  flex-direction: column;
`;

export const Styled = {
  TopBar,
  TopBarTitle,
  UserName,
  UserImage,
  UserImageContainer,
  ProfileButton,
  ProfileButtons
};
