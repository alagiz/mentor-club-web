import { IAppState } from "store";

export type Selector<output> = (state: IAppState) => output;
