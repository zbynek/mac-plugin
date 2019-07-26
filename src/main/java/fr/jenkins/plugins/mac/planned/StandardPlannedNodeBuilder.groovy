package fr.jenkins.plugins.mac.planned;

import java.util.concurrent.Future;

import com.google.common.util.concurrent.Futures;

import fr.jenkins.plugins.mac.MacUser
import fr.jenkins.plugins.mac.slave.MacSlave;
import fr.jenkins.plugins.mac.ssh.SSHCommand
import fr.jenkins.plugins.mac.ssh.SSHCommandException
import hudson.model.Descriptor;
import hudson.slaves.ComputerLauncher;
import hudson.slaves.NodeProvisioner;

/**
 * The default {@link PlannedNodeBuilder} implementation, in case there is other
 * registered.
 */
public class StandardPlannedNodeBuilder extends PlannedNodeBuilder {
    @Override
    public NodeProvisioner.PlannedNode build() {
        Future f;
        try {
            MacUser user = SSHCommand.createUserOnMac(cloud.macHost)
            ComputerLauncher launcher = cloud.connector.createLauncher(cloud.macHost, user)
            MacSlave agent = new MacSlave(cloud.name, label.toString(), user, launcher)
            f = Futures.immediateFuture(agent)
        } catch (IOException | Descriptor.FormException | SSHCommandException e) {
            f = Futures.immediateFailedFuture(e);
        }
        return new NodeProvisioner.PlannedNode(macHost.host, f, numExecutors)
    }
}
