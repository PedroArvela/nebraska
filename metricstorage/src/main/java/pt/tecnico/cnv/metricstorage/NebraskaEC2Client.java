package pt.tecnico.cnv.metricstorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;

public class NebraskaEC2Client {
	private AmazonEC2 ec2;
	private AmazonCloudWatchClient cloudWatch;
	private String endpoint;

	private NebraskaEC2Client(String endpoint) {
		this.endpoint = endpoint;
	}

	/**
	 * Returns a new AmazonEC2Client
	 * 
	 */
	public static NebraskaEC2Client init(String endpoint) throws AmazonClientException {
		NebraskaEC2Client amz = new NebraskaEC2Client(endpoint);

		AWSCredentials credentials = null;

		/**
		 * Initializes the AmazonEC2Client and AmazonCloudWatchClient with the
		 * required credentials.
		 * 
		 * Requires the <code>~/.aws/credentials</code> file to be properly
		 * placed and filled in.
		 */
		try {
			credentials = new ProfileCredentialsProvider().getCredentials();
		} catch (Exception e) {
			throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct "
					+ "location (~/.aws/credentials), and is in valid format.", e);
		}
		amz.ec2 = new AmazonEC2Client(credentials);
		amz.cloudWatch = new AmazonCloudWatchClient(credentials);

		return amz;
	}

	/**
	 * Returns a list of Reservations.
	 * 
	 * Reservations are requests to launch instances. A reservation can result
	 * in multiple instances.
	 */
	public List<Reservation> getReservations() {
		DescribeInstancesResult describeInstancesResult = ec2.describeInstances();
		List<Reservation> reservations = describeInstancesResult.getReservations();

		return reservations;
	}

	/**
	 * Returns the list of Instances launched by a reservation.
	 * */
	public Set<Instance> getInstances(Reservation reservation) {
		Set<Instance> instances = new HashSet<Instance>();
		instances.addAll(reservation.getInstances());

		return instances;
	}

	/**
	 * Returns the list of all Instances.
	 * */
	public Set<Instance> getInstances() {
		Set<Instance> instances = new HashSet<Instance>();
		List<Reservation> reservations = getReservations();

		for (Reservation reservation : reservations) {
			instances.addAll(this.getInstances(reservation));
		}

		return instances;
	}
}
