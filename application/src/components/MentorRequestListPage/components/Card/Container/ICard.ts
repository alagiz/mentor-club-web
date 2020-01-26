import { ICardDataItem } from "../../CardDataItem/Container/ICardDataItem";

export interface ICard {
  cardTitle: string;
  cardDataItems: ICardDataItem[];
  cardBodyFooter: any;
}
