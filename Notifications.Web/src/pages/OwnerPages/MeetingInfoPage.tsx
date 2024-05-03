/* eslint-disable @typescript-eslint/ban-types */
import { NavigatorProps } from 'App';
import React from 'react';
export interface MeetingInfoPageProps extends NavigatorProps {
}
class MeetingInfoPage extends React.Component<MeetingInfoPageProps, {}> {
    public constructor(props: MeetingInfoPageProps) {
        super(props);
    }
    public override render(): React.ReactNode {
        return (
        <div></div>
        );
    }
}
export default MeetingInfoPage;