import styled from "styled-components";

const CardDataItem = styled.div`
  width: 100%;
  display: flex;
  justify-content: space-between;
  overflow: hidden;

  .card-data-item-text {
    width: 200px;
    text-overflow: ellipsis;
    overflow: hidden;
    text-align: right;
  }
`;

const CardDataItemLabel = styled.div`
  font-weight: bold;
  margin-right: 5px;
`;

const CardDataItemValue = styled.div``;

export const Styled = {
  CardDataItem,
  CardDataItemLabel: CardDataItemLabel,
  CardDataItemValue: CardDataItemValue
};
