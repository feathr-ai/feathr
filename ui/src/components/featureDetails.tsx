import React, { useCallback, useEffect, useState } from 'react';
import { Button, Card, Col, Row } from 'antd';
import { FeatureAttributes, IFeatureDetail, InputAnchorFeatures } from "../models/model";
import { useHistory } from "react-router-dom";
import { fetchFeatureLineages } from "../api";

type FeatureDetailsProps = {
  feature?: FeatureAttributes;
};

const FeatureDetails: React.FC<FeatureDetailsProps> = ({ feature }) => {
  const history = useHistory();
  const navigateTo = useCallback((location) => history.push(location), [history]);
  const [lineages, setLineages] = useState<IFeatureDetail>();
  const fetchData = useCallback(
    async () => {
      if (feature) {
        const result = await fetchFeatureLineages(feature.qualifiedName);
        setLineages(result);
        console.log(result);
      }
    }, [feature]
  );

  useEffect(() => {
    fetchData();
  }, [fetchData])

  function renderInputFeatureList(features: InputAnchorFeatures[]) {
    console.log(features);
    return (
      <ul>
        { features.map((_) => (
          <Button type="link" onClick={ () => {
            navigateTo(`/features/${ _.uniqueAttributes.qualifiedName }`)
          } }>{ _.uniqueAttributes.qualifiedName }</Button>
        )) }
      </ul>
    );
  }

  return (
    <div className="site-card-wrapper">
      <Row gutter={ 16 }>
        { feature?.type &&
            <Col span={ 8 }>
                <Card title="Type" bordered={ false }>
                  { feature.type }
                </Card>
            </Col>
        }
        { feature?.definition &&
            <Col span={ 8 }>
                <Card title="Definition" bordered={ false }>
                  { feature.definition }
                </Card>
            </Col>
        }
        { feature?.def &&
            <Col span={ 8 }>
                <Card title="SQL Expression" bordered={ false }>
                  { feature["def.sqlExpr"] }
                </Card>
            </Col>
        }
        { feature?.input_anchor_features && feature?.input_anchor_features.length > 0 &&
            <Col span={ 16 }>
                <Card title="Input Anchor Features" bordered={ false }>
                  { renderInputFeatureList(feature.input_anchor_features) }
                </Card>
            </Col>
        }
        { feature?.input_derived_features && feature?.input_derived_features.length > 0 &&
            <Col span={ 16 }>
                <Card title="Input Derived Features" bordered={ false }>
                  { renderInputFeatureList(feature.input_derived_features) }
                </Card>
            </Col>
        }
        { feature?.transformation &&
            <Col span={ 8 }>
                <Card title="Transformation" bordered={ false }>
                  { feature.transformation.transform_expr }
                </Card>
            </Col>
        }
        { feature?.window &&
            <Col span={ 8 }>
                <Card title="Window" bordered={ false }>
                  { feature.window }
                </Card>
            </Col>
        }
      </Row>
    </div>
  );
};

export default FeatureDetails
