import { useEffect, useState } from 'react'

import { FormInstance, Form, SelectProps, message } from 'antd'
import { useNavigate } from 'react-router-dom'

import { fetchProjectLineages, createAnchorFeature, createDerivedFeature } from '@/api'
import { ValueType, TensorCategory, VectorType, NewFeature } from '@/models/model'

const valueOptions = ValueType.map((value: string) => ({
  value: value,
  label: value
}))

const tensorOptions = TensorCategory.map((value: string) => ({
  value: value,
  label: value
}))

const typeOptions = VectorType.map((value: string) => ({
  value: value,
  label: value
}))

export type Options = SelectProps['options']

export const enum FeatureEnum {
  Anchor,
  Derived
}

export const enum TransformationTypeEnum {
  Expression,
  Window,
  UDF
}

export const useForm = (form: FormInstance<any>) => {
  const navigate = useNavigate()

  const [createLoading, setCreateLoading] = useState<boolean>(false)

  const [loading, setLoading] = useState<boolean>(false)

  const [anchorOptions, setAnchorOptions] = useState<Options>([])
  const [anchorFeatureOptions, setAnchorFeatureOptions] = useState<Options>([])
  const [derivedFeatureOptions, setDerivedFeatureOptions] = useState<Options>([])

  const project = Form.useWatch('project', form)
  const featureType = Form.useWatch<FeatureEnum>('featureType', form)
  const selectTransformationType = Form.useWatch<TransformationTypeEnum>(
    'selectTransformationType',
    form
  )

  const fetchData = async (project: string) => {
    try {
      setLoading(true)
      form.setFieldValue('anchor', undefined)
      form.setFieldValue('anchorFeatures', undefined)
      form.setFieldValue('derivedFeatures', undefined)
      const { guidEntityMap } = await fetchProjectLineages(project)
      if (guidEntityMap) {
        const anchorOptions: Options = []
        const anchorFeatureOptions: Options = []
        const derivedFeatureOptions: Options = []

        Object.values(guidEntityMap).forEach((value: any) => {
          const { guid, typeName, attributes } = value
          const { name } = attributes
          switch (typeName) {
            case 'feathr_anchor_v1':
              anchorOptions.push({ value: guid, label: name })
              break
            case 'feathr_anchor_feature_v1':
              anchorFeatureOptions.push({ value: guid, label: name })
              break
            case 'feathr_derived_feature_v1':
              derivedFeatureOptions.push({ value: guid, label: name })
              break
            default:
              break
          }
        })

        setAnchorOptions(anchorOptions)
        setAnchorFeatureOptions(anchorFeatureOptions)
        setDerivedFeatureOptions(derivedFeatureOptions)
      }
    } catch {
      //
    } finally {
      setLoading(false)
    }
  }

  const onFinish = async (values: any) => {
    setCreateLoading(true)
    try {
      const tags = values.tags?.reduce((tags: any, item: any) => {
        tags[item.name] = item.value || ''
        return tags
      }, {} as any)

      const newFeature: NewFeature = {
        name: values.name,
        featureType: {
          dimensionType: values.dimensionType,
          tensorCategory: values.tensorCategory,
          type: values.type,
          valType: values.valType
        },
        tags,
        key: values.keys,
        inputAnchorFeatures: values.anchorFeatures,
        inputDerivedFeatures: values.derivedFeatures,
        transformation: {
          transformExpr: values.transformExpr,
          filter: values.filter,
          aggFunc: values.aggFunc,
          limit: values.limit,
          groupBy: values.groupBy,
          window: values.window,
          defExpr: values.defExpr,
          udfExpr: values.udfExpr
        }
      }

      if (values.featureType === FeatureEnum.Anchor) {
        await createAnchorFeature(project, values.anchor, newFeature)
      } else {
        await createDerivedFeature(project, newFeature)
      }
      message.success('New feature created')
      navigate(`/features?project=${project}`)
    } catch (err: any) {
      message.error(err.detail || err.message)
    } finally {
      setCreateLoading(false)
    }
  }

  useEffect(() => {
    if (project) {
      fetchData(project)
    }
  }, [project])

  useEffect(() => {
    form.setFieldsValue({
      featureType: FeatureEnum.Anchor,
      selectTransformationType: TransformationTypeEnum.Expression
    })
  }, [form])

  return {
    createLoading,
    loading,
    project,
    featureType,
    selectTransformationType,
    anchorOptions,
    anchorFeatureOptions,
    derivedFeatureOptions,
    valueOptions,
    tensorOptions,
    typeOptions,
    onFinish
  }
}
