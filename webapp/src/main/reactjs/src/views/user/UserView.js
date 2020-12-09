import React from 'react';
import ContentWithHeader from "../../components/ContentWithHeader";
import {Button} from "antd";

const loremIpsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur vehicula, tellus id rhoncus semper, nisl massa sollicitudin odio, id convallis dui orci ac sapien. Ut ut dapibus mi. In hac habitasse platea dictumst. Cras quis fermentum magna, ac ornare arcu. Donec volutpat sollicitudin nisi at rhoncus. Aliquam tempus mi eget auctor tristique. Integer dictum iaculis felis ac vulputate. Cras dignissim, massa id feugiat vehicula, dolor nibh egestas libero, eu lobortis augue mi ac mauris. Donec luctus tellus nec est eleifend, sit amet vestibulum augue pulvinar. Etiam lorem diam, pharetra vitae eleifend vitae, scelerisque eget eros.\n" +
    "\n" +
    "Fusce at lacus eleifend, bibendum ligula ut, tincidunt orci. In malesuada purus sem, et dictum odio convallis non. Donec a est id ipsum rutrum commodo. Mauris sit amet feugiat justo. Praesent tincidunt, nulla ultrices blandit rhoncus, eros nibh feugiat lorem, aliquam aliquam odio augue sed sapien. Donec sit amet quam laoreet dolor sagittis luctus eu in purus. Donec faucibus sodales metus in pharetra. Morbi hendrerit luctus dolor a hendrerit. Morbi hendrerit eget purus sed dictum. Morbi iaculis arcu sed sodales eleifend. Proin iaculis gravida ex ac fermentum. Cras fermentum quam non ipsum varius eleifend. Praesent in nulla eget ex aliquet tempus.\n" +
    "\n" +
    "Nulla malesuada aliquam lorem, et finibus nunc venenatis malesuada. Suspendisse ac sodales sem. Cras tempor, erat quis sagittis iaculis, eros tellus varius mi, non porttitor leo felis id elit. Donec at nisi porttitor, tempus lorem id, varius velit. Phasellus quis leo at libero tempor commodo eget faucibus arcu. Donec scelerisque sapien in elit elementum, sit amet consectetur sapien fermentum. Quisque molestie in mi vitae tristique. Proin volutpat nunc et mattis viverra. Suspendisse et est lorem. Nam ac metus sed sem imperdiet ullamcorper. Duis rutrum vehicula neque, sit amet semper justo. Proin eu odio et lorem eleifend gravida id et ligula. Nullam aliquet metus massa, nec luctus ipsum viverra eget. Praesent augue elit, consectetur eu pharetra nec, ultricies vitae lacus. Fusce rhoncus dictum nibh, sed aliquet lacus sodales sit amet.\n" +
    "\n" +
    "Aenean pellentesque molestie nisl ac vehicula. Aenean gravida sem id diam accumsan suscipit. Integer aliquet egestas dui vitae vestibulum. Maecenas hendrerit porta eleifend. Ut tincidunt lacinia orci, sit amet imperdiet magna interdum at. Suspendisse a justo ex. Morbi nec bibendum leo. Sed tellus mauris, condimentum sed imperdiet a, laoreet nec orci. Aliquam sit amet felis tincidunt, semper lacus eget, vestibulum odio. Nullam sagittis dictum urna in pharetra. Ut bibendum faucibus orci. Nulla venenatis auctor sodales.\n" +
    "\n" +
    "Nunc nec dui egestas, pellentesque purus vitae, tempus nisi. Aliquam nec sodales risus. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Aliquam volutpat sem nec velit congue malesuada. Morbi feugiat mollis enim at gravida. Ut interdum purus at porttitor vulputate. In hac habitasse platea dictumst. Nulla facilisi. Suspendisse potenti. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Duis lacinia pretium elit.";

function UserView({id}){
    console.log(id);

    return <ContentWithHeader
            title={"usuario"}
            actionComponents={[
                <Button>Remover</Button>,
                <Button>Editar perfil</Button>
            ]}
            content={<p>{loremIpsum}</p>}
        />
}

export default UserView;