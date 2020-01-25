export type IMentorRequestStatus =
  | "CREATED"
  | "IN_PROGRESS"
  | "DONE"
  | "FAILED";

export interface IMentorRequest {
  mentorRequestId: string;
  numberOfLots: number;
  numberOfWafers: number;
  durationInMinutes: number;
  status: IMentorRequestStatus;
  startedAt: string;
  endedAt: string;
}

export interface IMentorRequestsState {
  mentorRequests: IMentorRequest[] | null;
  mentorRequestResult: IMentorRequestResult | null;
  mentorRequestResultWafers: { [x: string]: IWaferDetails };
  isFetchingMentorRequests: boolean;
  isFetchingMentorRequestResultOverview: boolean;
  isFetchingMentorRequestResultWafer: boolean;
  isCreatingMentorRequest: boolean;
  fetchMentorRequestsError: string | null;
  fetchMentorRequestResultOverviewError: string | null;
  fetchMentorRequestResultWaferError: string | null;
  createMentorRequestError: string | null;
  newMentorRequestId: string | null;
}

export interface IFetchMentorRequestResultsOverviewRequest {
  mentorRequestId: string;
}

export interface IFetchMentorRequestResultsOverviewResponse {
  mentorRequestResult: IMentorRequestResult;
}

export interface IMentorRequestResult {
  mentorRequestId: string;
  numberOfLots: number;
  numberOfWafers: number;
  durationInMinutes: number;
  status: IMentorRequestStatus;
  startedAt: string;
  endedAt: string;
  wafers: IResultWafer[];
}

export interface IResultWafer {
  waferId: string;
  wmsId: string;
  fittedRange: number;
  residual: number;
  chamberId: string;
  metrologyStartTime: string;
}

export interface IWaferDetails {
  id: string;
  waferID: string | null;
  lotID: string | null;
  chuckID: string | null;
  operationParameters: object;
  parentId: string | null;
  exposedFields: IExposedFields;
  waferPoints: IWaferPoints;
}

interface IExposedFields {
  width: number;
  height: number;
  diam: number;
  fields: IWaferField[];
}

export interface IWaferPointsValue {
  id: number;
  cfx: number;
  cfy: number;
  ifx: number;
  ify: number;
  val: number;
  valX: number | undefined | null;
  valY: number | undefined | null;
  validX: boolean;
  validY: boolean;
  xPos: number;
  yPos: number;
  radius: number;
}

interface IWaferPoints {
  stats?: any[];
  values: IWaferPointsValue[];
}

interface IWaferField {
  id: number;
  posX: number;
  posY: number;
  scan: string;
  scanPath: string;
}

export interface IFetchMentorRequestResultsDetailRequest {
  mentorRequestId: string;
  wmsId: string;
}

export interface IFetchMentorRequestResultsDetailResponse {
  wafer: IWaferDetails;
}

export interface IRunMentorRequestRequest {
  rho: number;
  targetLabel: string;
  parameterName: string;
  featureAngle: number;
  healthFilterSettingsMax?: number;
  healthFilterSettingsNSigma?: number;
  residualOutlierRemovalSettingsNSigma?: number;
  healthFilterSettingsEdgeClearance?: number;
  userId: string;
}

export interface IRunMentorRequestResponse {
  mentorRequestId: string;
}
