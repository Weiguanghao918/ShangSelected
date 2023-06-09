package cn.itedus.ssyx.helper;

import cn.itedus.ssyx.model.acl.Permission;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-09 15:29
 * @description: 菜单服务帮助类
 */
public class PermissionHelper {

    /**
     * 获取菜单列表集合
     *
     * @param allPermissionList 所有的菜单项目
     * @return 封装了子节点的菜单集合
     */
    public static List<Permission> builder(List<Permission> allPermissionList) {
        List<Permission> treeList = new ArrayList<>();
        for (Permission treeNode : allPermissionList) {
            if (treeNode.getPid() == 0) {
                treeNode.setLevel(1);
                treeList.add(findChilder(treeNode, allPermissionList));
            }
        }
        return treeList;
    }

    /**
     * 获取每个节点的子节点
     *
     * @param treeNode          当前节点
     * @param allPermissionList 所有的菜单项
     * @return 封装了子节点的菜单项
     */
    private static Permission findChilder(Permission treeNode, List<Permission> allPermissionList) {
        treeNode.setChildren(new ArrayList<Permission>());

        for (Permission it : allPermissionList) {
            if (treeNode.getId().longValue() == it.getPid().longValue()) {
                it.setLevel(treeNode.getLevel() + 1);
                if (treeNode.getChildren() == null) {
                    treeNode.setChildren(new ArrayList<Permission>());
                }
                treeNode.getChildren().add(findChilder(it, allPermissionList));
            }
        }
        return treeNode;
    }
}
