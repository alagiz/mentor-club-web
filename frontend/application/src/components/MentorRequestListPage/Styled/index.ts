import styled from "styled-components";
import { SpinnerBoundaries } from "./SpinnerBoundaries";

const Container = styled.div`
  padding: 15px;

  .ant-spin-nested-loading {
    height: 200px;
  }
`;

const MentorRequestList = styled.div`
  display: flex;
  flex-wrap: wrap;
  padding-bottom: 20px;
`;

const CardContainer = styled.div`
  padding: 10px;

  .ant-card-bordered {
    border: 0;
  }

  .mentor-request-list-page-card {
    width: 300px;
  }
`;

const UserImage = styled.img`
  margin-top: -3px;
  width: 3rem;
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

export const Styled = {
  Container,
  TopHeaderContainer,
  TopHeader,
  SpinnerBoundaries,
  MentorRequestList,
  CardContainer,
  UserImage
};
