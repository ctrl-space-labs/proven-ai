import React from 'react';

// ** Custom Menu Components
import VerticalNavLink from './VerticalNavLink'
import VerticalNavGroup from './VerticalNavGroup'
import VerticalNavSectionTitle from './VerticalNavSectionTitle'
import VerticalNavButton from 'src/navigation/vertical/VerticalNavButton'

const { NewDataPodButton, NewAgentButton } = VerticalNavButton;


const resolveNavItemComponent = item => {
  if (item.sectionTitle) return VerticalNavSectionTitle
  if (item.children) return VerticalNavGroup
  

  return VerticalNavLink
}

const VerticalNavItems = props => {
  // ** Props
  const { verticalNavItems } = props

  const RenderMenuItems = verticalNavItems?.map((item, index) => {
    const TagName = resolveNavItemComponent(item)

    // return <TagName {...props} key={index} item={item} />
    return (
      <React.Fragment key={index}>
        <TagName {...props} item={item} />                
        {item.sectionTitle && item.sectionTitle === "DATA PODS" && <NewDataPodButton />}
        {item.sectionTitle && item.sectionTitle === "AI AGENTS" && <NewAgentButton />}
      </React.Fragment>
    )
  })

  return <>{RenderMenuItems}</>
}

export default VerticalNavItems
