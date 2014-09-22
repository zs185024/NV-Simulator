package simulator.mapping;

import java.util.ArrayList;
import java.util.HashMap;

import simulator.network.components.physical.PhysicalLink;
import simulator.network.components.physical.PhysicalNode;
import simulator.network.components.virtual.VirtualLink;
import simulator.network.components.virtual.VirtualNode;

/**
 *  Virtual Network Mapping Over Substrate Network
 */
public class Mapping {
  private HashMap<VirtualNode, PhysicalNode> nodesMapping;
  private HashMap<VirtualLink, ArrayList<PhysicalLink>> linksMapping;

  public Mapping() {
    nodesMapping = new HashMap<VirtualNode, PhysicalNode>();
    linksMapping = new HashMap<VirtualLink, ArrayList<PhysicalLink>>();
  }

  public void addNodeMapping(VirtualNode virtualNode, PhysicalNode physicalNode) {
    if(nodesMapping.containsKey(virtualNode))
      throw new RuntimeException("Nó virtual já está alocado.");

    physicalNode.addLoad(virtualNode.getCapacity());
    nodesMapping.put(virtualNode, physicalNode);
  }

  public void addLinkMapping(VirtualLink virtualLink,
                                         ArrayList<PhysicalLink> physicalLinks) {
    if(linksMapping.containsKey(virtualLink))
      throw new RuntimeException("Enlace virtual já está alocado.");

    for(PhysicalLink physicalLink : physicalLinks) {
      physicalLink.addBandwidthLoad(virtualLink.getBandwidthCapacity());
    }
    linksMapping.put(virtualLink, physicalLinks);
  }

  public boolean isNodeMapped(VirtualNode virtualNode) {
    return nodesMapping.containsKey(virtualNode);
  }

  public boolean isLinkMapped(VirtualLink virtualLink) {
    return linksMapping.containsKey(virtualLink);
  }

  public PhysicalNode getHostingNodeFor(VirtualNode virtualNode) {
    return nodesMapping.get(virtualNode);
  }

  public ArrayList<PhysicalLink> getHostingLinksFor(VirtualLink virtualLink) {
    return linksMapping.get(virtualLink);
  }

  public boolean isNodeInUse(PhysicalNode node) {
    return nodesMapping.values().contains(node);
  }

  public boolean isLinkInUse(PhysicalLink link) {
    for(ArrayList<PhysicalLink> linksPath : linksMapping.values()) {
      for(PhysicalLink l : linksPath) {
        if(l.equals(link)) {

          return true;
        }
      }
    }

    return false;
  }

  public void clearMappings() {
    for(VirtualNode virtualNode : nodesMapping.keySet()) {
      PhysicalNode hostingNode = nodesMapping.get(virtualNode);
      hostingNode.removeLoad(virtualNode.getCapacity());
    }
    for(VirtualLink virtualLink : linksMapping.keySet()) {
      ArrayList<PhysicalLink> hostingLinks = linksMapping.get(virtualLink);
      for(PhysicalLink hostingLink : hostingLinks) {
        hostingLink.removeBandwidthLoad(virtualLink.getBandwidthCapacity());
      }
    }

    nodesMapping.clear();
    linksMapping.clear();
  }

  /**
   *
   * CÓDIGO LEGADO
   *
   */
  // add node mapping
    // physicalNode.allocVirtualNode(request.getId(), virtualNode);
    // virtualNode.setPhysicalHostNode(physicalNode);

  // clear mappings
    // for(PhysicalNode node : nodesMapping.values()) {
    //   node.desAllocRequest(request.getId());
    // }
    // for(PhysicalLink[] linksPath : linksMapping.values()) {
    //   for(PhysicalLink link : linksPath) {
    //     link.desAllocVirtualLink(request.getId());
    //   }
    // }
}