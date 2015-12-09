package com.example.derekchiu.q;

import com.parse.*;

import java.util.List;
import android.util.Log;


/**
 * Created by DopeDev on 11/30/15.
 */
public class DBUtil {

    public static void addSelfToQueue(final String company, final String myId, final SaveCallback callback) {

        getPlaceInQueue(company, myId, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("QueuePlace");
                    query.whereEqualTo("company", company);
                    query.orderByDescending("place");

                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                        public void done(ParseObject object, ParseException e) {
                            int place = 0;
                            if (object == null) {
                                Log.d("place", "The getFirst request failed.");
                            } else {
                                place = object.getNumber("place").intValue() + 1;
                            }

                            ParseObject queuePlace = new ParseObject("QueuePlace");
                            queuePlace.put("userID", myId);
                            queuePlace.put("company", company);
                            queuePlace.put("place", place);
                            ParseACL acl = new ParseACL();
                            acl.setPublicReadAccess(true);
                            acl.setPublicWriteAccess(true);
                            queuePlace.setACL(acl);
                            queuePlace.saveInBackground(callback);
                        }
                    });
                } else {
                    Log.d("place", "Can't add self because I already exist");
                    callback.done(null);
                }

            }
        });


    }

    public static void getPlaceInQueue(final String company, String myId, final GetCallback<ParseObject> callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("QueuePlace");

        query.whereEqualTo("company", company);
        query.whereEqualTo("userID", myId);

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(final ParseObject queuePlaceObject, ParseException e) {
                if (queuePlaceObject == null) {
                    callback.done(null, null);
                } else {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Company");

                    query.whereEqualTo("name", company);

                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                        public void done(ParseObject object, ParseException e) {
                            if (object == null) {
                                callback.done(null, null);
                            } else {
                                int queueStart = queuePlaceObject.getInt("place") - object.getInt("queueStart");
                                ParseObject returnObj = new ParseObject("QueuePlace");
                                returnObj.put("place", queueStart);
                                callback.done(returnObj, null);
                            }
                        }
                    });
                }
            }
        });
    }

    public static void removeSelfFromQueue(final String company, String myId, final DeleteCallback callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("QueuePlace");

        query.whereEqualTo("company", company);
        query.whereEqualTo("userID", myId);

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(final ParseObject queuePlaceObject, ParseException e) {
                if (queuePlaceObject == null) {
                    Log.d("Remove", "Already removed");
                } else {
                    int currPlace = queuePlaceObject.getInt("place");
                    Log.d("d", "curr " + currPlace);
                    queuePlaceObject.deleteInBackground(callback);

                    ParseQuery<ParseObject> query = ParseQuery.getQuery("QueuePlace");
                    query.whereGreaterThan("place", currPlace);
                    query.whereEqualTo("company", company);
                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> objectList, ParseException e) {
                            if (e == null) {
                                for (int i = 0; i < objectList.size(); i++) {
                                    ParseObject object = objectList.get(i);
                                    object.put("place", object.getInt("place") - 1);
                                    object.saveInBackground();

                                }
                            } else {
                                Log.d("score", "Error: " + e.getMessage());
                            }
                        }
                    });
                }
            }
        });
    }

    public static void bumpBackInQueue(final String company, String myId, final SaveCallback callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("QueuePlace");

        query.whereEqualTo("company", company);
        query.whereEqualTo("userID", myId);

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(final ParseObject queuePlaceObject, ParseException e) {
                if (queuePlaceObject == null) {
                    Log.d("Remove", "Already removed");
                } else {
                    final int currPlace = queuePlaceObject.getInt("place");
                    Log.d("d", "curr " + currPlace);

                    ParseQuery<ParseObject> query = ParseQuery.getQuery("QueuePlace");
                    query.whereGreaterThan("place", currPlace);
                    query.whereEqualTo("company", company);
                    query.orderByAscending("place");
                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> objectList, ParseException e) {
                            if (e == null) {
                                int count = 0;
                                for (int i = 0; i < objectList.size() && count < 5; i++) {
                                    ParseObject object = objectList.get(i);
                                    object.put("place", object.getInt("place") - 1);
                                    object.saveInBackground();
                                    count += 1;
                                }

                                queuePlaceObject.put("place", currPlace + count);
                            } else {
                                Log.d("score", "Error: " + e.getMessage());
                            }
                        }
                    });
                }
            }
        });
    }

    public static void getInfoAboutMe(String myId, final GetCallback callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("QUser");

        query.whereEqualTo("userID", myId);

        query.getFirstInBackground(callback);
    }

    public static void getQueue(String company, final FindCallback callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("QueuePlace");
        query.whereEqualTo("company", company);
        query.orderByAscending("place");
        query.findInBackground(callback);
    }

    public static void saveRecruiter(String myId, String name, String company, String lookingFor) {
        ParseObject recruiter = new ParseObject("Recruiter");
        recruiter.put("userID", myId);
        recruiter.put("company", company);
        recruiter.put("lookingFor", lookingFor);
        recruiter.put("name", name);
        ParseACL acl = new ParseACL();
        acl.setPublicReadAccess(true);
        acl.setPublicWriteAccess(true);
        recruiter.setACL(acl);
        recruiter.saveInBackground();
    }

    public static void getCompanies(final FindCallback callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Company");
        query.orderByAscending("name");
        query.findInBackground(callback);
    }

    public static void getQueuesUserIsPartOf(String myId, final FindCallback callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("QueuePlace");
        query.whereEqualTo("userID", myId);
        query.findInBackground(callback);
    }
    
    public static void getRidOfFirstPersonInQueue(String company) {

        ParseQuery<ParseObject> moveUpQuery = ParseQuery.getQuery("QueuePlace");
        moveUpQuery.whereEqualTo("company", company);
        moveUpQuery.orderByAscending("place");
        moveUpQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objectList, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objectList.size(); i++) {
                        ParseObject object = objectList.get(i);

                        if (i == 0) {
                            object.deleteInBackground();
                        } else {
                            object.put("place", object.getInt("place") - 1);
                            object.saveInBackground();
                        }
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }

}