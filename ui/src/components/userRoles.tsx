import React, { useCallback, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button, Modal, PageHeader, Row, Space, Table, Tag } from "antd";
import { IUserRole } from "../models/model";
import { listUserRole } from "../api";
import { useMsal } from '@azure/msal-react';

const UserRoles: React.FC = () => {
    const navigate = useNavigate();
    const [visible, setVisible] = React.useState(false);
    const [confirmLoading, setConfirmLoading] = React.useState(false);
    const [modalText, setModalText] = React.useState('Content of the modal');

    const { accounts, instance } = useMsal();
    const showModal = ()  => {
        setVisible(true);
        setModalText("This Role Assignment will be deleted.");
    };
    const handleOk = () => {
        setModalText('The modal will be closed after two seconds');
        setConfirmLoading(true);
        setTimeout(() => {
            setVisible(false);
            setConfirmLoading(false);
        }, 2000);
    };

    const handleCancel = () => {
        console.log('Clicked cancel button');
        setVisible(false);
    };
    const columns = [
        {
            title: <div>Scope</div>,
            dataIndex: 'scope',
            key: 'scope',
            align: 'center' as 'center',
        },
        {
            title: <div>User</div>,
            dataIndex: 'userName',
            key: 'userName',
            align: 'center' as 'center',
        },
        {
            title: <div style={{ userSelect: "none" }}>Role</div>,
            dataIndex: 'roleName',
            key: 'roleName',
            align: 'center' as 'center',
        },
        {
            title: <div>Permissions</div>,
            key: 'permissions',
            dataIndex: 'permissions',
            render: (tags: any[]) => (
                <>
                    {tags.map(tag => {
                        let color = tag.length > 5 ? 'red' : 'green';
                        if (tag === 'Write') color = 'blue'
                        return (
                            <Tag color={color} key={tag}>
                                {tag.toUpperCase()}
                            </Tag>
                        );
                    })}
                </>
            ),
        },
        {
            title: <div>Create Reason</div>,
            dataIndex: 'createReason',
            key: 'createReason',
            align: 'center' as 'center',
        },
        {
            title: <div>Create Time</div>,
            dataIndex: 'createTime',
            key: 'createTime',
            align: 'center' as 'center',
        },
        {
            title: 'Action',
            key: 'action',
            render: (text: any) => (
                <Space size="middle">
                    <Button type="primary" onClick={showModal}>
                        Delete
                    </Button>
                    <Modal
                        title="Please Confirm"
                        visible={visible}
                        onOk={handleOk}
                        confirmLoading={confirmLoading}
                        onCancel={handleCancel}
                    >
                        <p>{modalText}</p>
                    </Modal>
                </Space>
            ),
        },
    ];
    const [page, setPage] = useState(1);
    const [loading, setLoading] = useState(false);
    const [tableData, setTableData] = useState<IUserRole[]>();

    const fetchData = useCallback(async () => {
        setLoading(true);
        const result = await listUserRole();
        console.log(result);
        setPage(page);
        setTableData(result);
        setLoading(false);
    }, [page])

    const onClickRoleAssign = () => {
        navigate('/role-management');
        return;
    }

    useEffect(() => {
        fetchData()
    }, [])

    return (
        <div>
            <PageHeader
                title={`Role Managements`}
                style={{ backgroundColor: "white", paddingLeft: "50px", paddingRight: "50px" }}>
                <Row>
                    <div style={{ flex: 1 }}>
                        <>
                            <p>
                                Below is the mock data for now. Will connect with Management APIs.
                            </p>
                        </>
                    </div>
                </Row>
            </PageHeader>
            <Space style={{ marginBottom: 16 }}>
                <Button type="primary" onClick={onClickRoleAssign}
                    style={{ position: "absolute", right: "12px", top: "56px" }}>
                    + Create Role Assignment
                </Button>
            </Space>
            <Table dataSource={tableData} columns={columns} />;
        </div>
    );
}

export default UserRoles;
