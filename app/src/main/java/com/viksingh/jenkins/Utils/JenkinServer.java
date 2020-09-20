package com.viksingh.jenkins.Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JenkinServer {

    public static void buildJob(String jenkinsUrl, String userName, String password,String jobName, Map<String, String> paramMap) {
        JenkinsServer jenkinsServer = null;
        try {
            jenkinsServer = new JenkinsServer(new URI(jenkinsUrl),userName,password);
            jenkinsServer.getJob(jobName).build(paramMap);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

    }

    public static void buildJobWithoutParam(String jenkinsUrl, String userName, String password,String jobName) {

        JenkinsServer jenkinsServer = null;
        try {
            jenkinsServer = new JenkinsServer(new URI(jenkinsUrl),userName,password);
            jenkinsServer.getJob(jobName).build();
        } catch (URISyntaxException | IOException  e) {
            e.printStackTrace();
        }
    }

    public static String computerAllDetails(String jenKinsUrl, String userName, String passWord, String computerName) {
        String json=null;
        try {
            JenkinsServer jenkinsServer = new JenkinsServer(new URI(jenKinsUrl),userName,passWord);
            json = convertMapToJson(jenkinsServer.getComputers().get(computerName).details().getMonitorData());
        } catch (URISyntaxException | IOException  e) {
            e.printStackTrace();
        }
        return json;
    }

    public static String convertMapToJson(Map<String, Map> paramMap) {
        String  json=null;

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            json = objectMapper.writeValueAsString(paramMap);
        }catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static Map<String, Job> getAllJobs(String jenkinsUrl, String userName, String passWord) {
        Map<String, Job> map=new HashMap<>();
        try {
            JenkinsServer jenkinsServer = new JenkinsServer(new URI(jenkinsUrl),userName,passWord);
            map = jenkinsServer.getJobs();
        } catch (URISyntaxException | IOException  e) {
            e.printStackTrace();
        }
        return map;
    }

    public static ArrayList<String> getComputerDeatail(String jenkinsUrl, String userName, String passWord) {
        ArrayList<String> arrayList = new ArrayList();
        new HashMap<>();
        try {
            JenkinsServer jenkinsServer = new JenkinsServer(new URI(jenkinsUrl),userName,passWord);
            Iterator<String> iterator = jenkinsServer.getComputers().keySet().iterator();
            while (iterator.hasNext())
                arrayList.add(iterator.next());
        } catch (URISyntaxException|IOException uRISyntaxException) {
            uRISyntaxException.printStackTrace();
        }
        return arrayList;
    }

    public static String getDomainName(String url) {
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        String domain = uri.getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }

    public static List<Build> getJobHistory(String jenkinsUrl, String userName, String passWord, String jobName) {
        List<Build> list=new ArrayList<>();
        try {
            JenkinsServer jenkinsServer = new JenkinsServer(new URI(jenkinsUrl),userName,passWord);
            list = jenkinsServer.getJob(jobName).details().getAllBuilds();
        } catch (URISyntaxException uRISyntaxException) {
            uRISyntaxException.printStackTrace();
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
        return list;
    }

    public static List<Build> getJobStatus(String jenkinsUrl, String userName, String passWord, String jobName) {
        List<Build> list=new ArrayList<>();
        try {
            JenkinsServer jenkinsServer = new JenkinsServer(new URI(jenkinsUrl),userName,passWord);
            list = jenkinsServer.getJob(jobName).details().getAllBuilds();
        } catch (URISyntaxException uRISyntaxException) {
            uRISyntaxException.printStackTrace();
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
        return list;
    }

    public static Map<String, String> getParameter(String jenkinsUrl, String userName, String passWord, String jobName) {

        System.out.println(jenkinsUrl+" "+userName+" "+passWord+" "+jobName);
        Map<String, String> hashMap = new HashMap<>();
        try {
            JenkinsServer jenkinsServer = new JenkinsServer(new URI(jenkinsUrl),userName,passWord);
            JobWithDetails jobWithDetails = jenkinsServer.getJob(jobName);
            if (jobWithDetails.details().hasFirstBuildRun() && !jobWithDetails.getLastBuild().details().getParameters().isEmpty()) {
                hashMap.putAll(jobWithDetails.getLastSuccessfulBuild().details().getParameters());
            } else {
                hashMap.put(null, null);
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return hashMap;
    }

    public static boolean startServer(String jenkinsUrl, String username, String password) {
        try {
            JenkinsServer jenkinsServer = new JenkinsServer(new URI(jenkinsUrl),username,password);
            if(jenkinsServer.isRunning()) {
                return true;
            }else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static  Map<String,Object> getAboutJenkins(String jenkinsUrl, String username, String password) {
        Map<String, Object> aboutJenkinsMap = new HashMap<>();
        if (aboutJenkinsMap.isEmpty()) {
            try {
                JenkinsServer jenkinsServer = new JenkinsServer(new URI(jenkinsUrl), username, password);
                String version = jenkinsServer.getComputerSet().getClient().getJenkinsVersion();
                int busyExecutors = jenkinsServer.getComputerSet().getBusyExecutors();
                int totalNoofExecutors = jenkinsServer.getComputerSet().getTotalExecutors();
                aboutJenkinsMap.put("version", version);
                aboutJenkinsMap.put("busyExecutors", busyExecutors);
                aboutJenkinsMap.put("totalNoofExecutors", totalNoofExecutors);
            } catch (URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        } else {
            return aboutJenkinsMap;
        }
        return aboutJenkinsMap;
    }

    public static  String getExecutors(String jenkinsUrl, String username, String password, String computerName) {
        int executots=0;
        JenkinsServer jenkinsServer = null;
        try {
            jenkinsServer = new JenkinsServer(new URI(jenkinsUrl), username, password);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        try {
            executots=jenkinsServer.getComputers().get(computerName).details().getNumExecutors();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return String.valueOf(executots);
    }
}
