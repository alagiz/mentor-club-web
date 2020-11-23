import styled from "styled-components";
import { SpinnerBoundaries } from "./SpinnerBoundaries";
import { Button } from "antd";

const Container = styled.div`
  padding: 15px;
  height: calc(100% - 45px);

  .ant-spin-nested-loading {
    height: 50px;
  }
`;

const NavigationButton = styled(Button)`
  border-radius: 0;
  bottom: 10px;
  right: 10px;
  min-width: 11rem;
`;

const ButtonContainer = styled.div`
  display: flex;
  justify-content: flex-end;
  margin-top: 10px;
`;

const TopHeaderContainer = styled.div`
  display: flex;
  justify-content: space-between;
  color: white;
`;

const TopHeader = styled.div`
  font-size: 20px;
  color: white;
`;

const FormAndButtonContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 100%;
`;

const FormContainer = styled.div`
  display: flex;
  flex-direction: column;
`;

const CardContainer = styled.div`
  display: flex;
  flex-direction: column;
  width: 100%;
  max-width: 400px;
  align-self: center;

  .ant-card-bordered {
    border: 0;
  }
`;

const UserImage = styled.img`
  margin-top: -3px;
  width: 3rem;
`;

export const Styled = {
  Container,
  TopHeaderContainer,
  TopHeader,
  SpinnerBoundaries,
  NavigationButton,
  ButtonContainer,
  FormAndButtonContainer,
  FormContainer,
  CardContainer,
  UserImage
};
