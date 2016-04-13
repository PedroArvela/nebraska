package pt.tecnico.cnv.loadbalancer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;

public class App {
	static AmazonEC2 ec2;
	static AmazonCloudWatchClient cloudWatch;

	public static void init() throws Exception {
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
		ec2 = new AmazonEC2Client(credentials);
		cloudWatch = new AmazonCloudWatchClient(credentials);
	}

	public static void main(String[] args) throws Exception {
		// Initialize the EC2 Client and CloudWatch Client
		init();

		try {
			// Set the US Oregon endpoint, which is the one used so far
			// The complete list is at
			// https://docs.aws.amazon.com/general/latest/gr/rande.html
			ec2.setEndpoint("ec2.us-west-2.amazonaws.com");
			cloudWatch.setEndpoint("monitoring.us-west-2.amazonaws.com");

			// Get list of reservations
			DescribeInstancesResult describeInstancesResult = ec2.describeInstances();
			List<Reservation> reservations = describeInstancesResult.getReservations();
			Set<Instance> instances = new HashSet<Instance>();

			// Get list of instances from reservations
			for (Reservation reservation : reservations) {
				instances.addAll(reservation.getInstances());
			}

			System.out.println("Total reservations:\t" + reservations.size());
			System.out.println("Total instances:\t" + instances.size());

		} catch (AmazonServiceException e) {
			System.out.println("Caught Exception: " + e.getMessage());
			System.out.println("Reponse Status Code: " + e.getStatusCode());
			System.out.println("Error Code: " + e.getErrorCode());
			System.out.println("Request ID: " + e.getRequestId());
		}

	}
}
