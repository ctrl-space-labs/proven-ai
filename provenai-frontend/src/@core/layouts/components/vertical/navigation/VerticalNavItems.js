// ** Custom Menu Components
import VerticalNavLink from './VerticalNavLink'
import VerticalNavSectionTitle from './VerticalNavSectionTitle'
import React from "react";
import VerticalNavButtons from "src/layouts/components/VerticalNavButton";


const { NewAgentButton, NewDataPodButton } = VerticalNavButtons;

const resolveNavItemComponent = item => {
  if (item.sectionTitle) return VerticalNavSectionTitle
  if (item.sectionButton === "dataPod") return NewDataPodButton
  if (item.children) return VerticalNavGroup

  return VerticalNavLink
}

const VerticalNavItems = props => {
  // ** Props
  const { verticalNavItems } = props

  const RenderMenuItems = verticalNavItems?.map((item, index) => {
    const TagName = resolveNavItemComponent(item)

    return (
      <React.Fragment key={index}>
        <TagName {...props} key={`${index}-item`} item={item}/>
        {item.sectionTitle && item.sectionTitle === "DATA PODS" && <NewDataPodButton/>}
        {item.sectionTitle && item.sectionTitle === "AI AGENTS" && <NewAgentButton/>}
      </React.Fragment>
    )
  })

  return <>{RenderMenuItems}</>
}

export default VerticalNavItems
