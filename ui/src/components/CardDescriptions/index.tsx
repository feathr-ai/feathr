import React from "react";
import { Card, Descriptions } from "antd";

export interface CardDescriptionsProps {
  title?: string;
  mapping: any[];
  descriptions: any;
}

const CardDescriptions = (props: CardDescriptionsProps) => {
  const { title, mapping, descriptions } = props;

  return descriptions ? (
    <Card className="card" title={title}>
      <Descriptions column={1}>
        {mapping.reduce((list: any, item) => {
          const value = descriptions?.[item.key];
          if (value) {
            list.push(
              <Descriptions.Item key={item.key} label={item.label}>
                {typeof value === "string" ? value : JSON.stringify(value)}
              </Descriptions.Item>
            );
          }
          return list;
        }, [])}
      </Descriptions>
    </Card>
  ) : null;
};

export default CardDescriptions;
