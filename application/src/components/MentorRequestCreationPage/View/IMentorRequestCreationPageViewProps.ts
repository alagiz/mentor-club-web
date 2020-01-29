import React, { ChangeEvent, SetStateAction } from "react";
import { IMentorRequestCreationPageProps } from "../Container/IMentorRequestCreationPageProps";
import { IUser } from "../../../state/MentorRequests/types";

export interface IMentorRequestCreationPageViewProps {
  mentors: IUser[] | null;
  selectedMentor: IUser | null;
  setSelectedMentor: React.Dispatch<SetStateAction<IUser | null>>;
  createMentorRequest: () => void;
  isLoading: IMentorRequestCreationPageProps["isFetchingMentorList"];
  mentorRequestDescription: string;
  updateMentorRequestDescription: (
    event: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => void;
}
