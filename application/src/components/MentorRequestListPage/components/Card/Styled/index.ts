import styled from "styled-components";
import { Card } from "antd";

const AntdCard = styled(Card)`
  width: 300px;
  height: 300px;
  border-radius: 0;

  .ant-card-head {
    display: flex;
    justify-content: center;
    text-align: center;
    height: 20px;
    background: #2f8e49;
    color: white;
    border-radius: 0;
  }

  .ant-card-body {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    padding: 10px;
    height: calc(100% - 46px);
    overflow: hidden;
  }
`;

const AntdCardBody = styled.div`
  display: flex;
  flex-direction: column;
  height: 100%;
`;

const AntdCardBodyDataItem = styled.div`
  width: 100%;
  display: flex;
  justify-content: space-between;
`;

const AntdCardBodyFooter = styled.div`
  display: flex;
`;

export const Styled = {
  AntdCard,
  AntdCardBody,
  AntdCardBodyFooter,
  AntdCardBodyDataItem
};
