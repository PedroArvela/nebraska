package pt.tecnico.cnv.loadbalancer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancingClient;
import com.amazonaws.services.elasticloadbalancing.model.CreateLoadBalancerRequest;
import com.amazonaws.services.elasticloadbalancing.model.CreateLoadBalancerResult;
import com.amazonaws.services.elasticloadbalancing.model.Listener;
import com.amazonaws.services.elasticloadbalancing.model.RegisterInstancesWithLoadBalancerRequest;
import com.amazonaws.services.elasticloadbalancing.model.RegisterInstancesWithLoadBalancerResult;

public class App {
	public static void main(String[] args) {
		AmazonEC2Client ec2 = new AmazonEC2Client();
		AmazonElasticLoadBalancingClient elb = new AmazonElasticLoadBalancingClient();

		// Create Load Balancer
		CreateLoadBalancerRequest lbRequest = new CreateLoadBalancerRequest();
		lbRequest.setLoadBalancerName("loader");
		List<Listener> listeners = new ArrayList<Listener>(1);
		listeners.add(new Listener("HTTP", 80, 80));
		lbRequest.withAvailabilityZones("us-west-2");
		lbRequest.setListeners(listeners);

		CreateLoadBalancerResult lbResult = elb.createLoadBalancer(lbRequest);
		System.out.println("created load balancer loader");

//		// Get running instances
//		DescribeInstancesResult describeInstancesRequest = ec2.describeInstances();
//		List<Reservation> reservations = describeInstancesRequest.getReservations();
//
//		List<Instance> instances = new ArrayList<Instance>();
//
//		for (Reservation reservation : reservations) {
//			instances.addAll(reservation.getInstances());
//		}
//
//		// Get instance id's
//		String id;
//		List instanceId = new ArrayList();
//		List instanceIdString = new ArrayList();
//
//		for (Instance in : instances) {
//			id = in.getInstanceId();
//			instanceId.add(new com.amazonaws.services.elasticloadbalancing.model.Instance(id));
//			instanceIdString.add(id);
//		}
//
//		// Register the instances to the balancer
//		RegisterInstancesWithLoadBalancerRequest register = new RegisterInstancesWithLoadBalancerRequest();
//		register.setLoadBalancerName("loader");
//		register.setInstances((Collection) instanceId);
//		RegisterInstancesWithLoadBalancerResult registerWithLoadBalancerResult = elb
//				.registerInstancesWithLoadBalancer(register);
	}
}
