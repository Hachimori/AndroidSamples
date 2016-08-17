package com.example.hachimori.retrofitsample.model;

/**
 * Created by benhachimori on 2016/08/07.
 */
public class Commits {
    private String url;
    private String sha;
    private Commit commit;
    
    public String getUrl() {
        return url;
    }

    public String getSha() {
        return sha;
    }
    
    public String getMessage() {
        return commit.getMessage();
    }
    
    public String getCommiterName() {
        return commit.getCommiterName();
    }
    
    public String getCommiterEmail() {
        return commit.getCommiterEmail();
    }
    
    public String getCommitDate() {
        return commit.getCommitDate();
    }
    
    private class Commit {
        private String message;
        private Committer committer;
        
        public String getMessage() {
            return message;
        }
        
        public String getCommiterName() {
            return committer.getName();
        }
        
        public String getCommiterEmail() {
            return committer.getEmail();
        }
        
        public String getCommitDate() {
            return committer.getDate();
        }
        
        private class Committer {
            private String name;
            private String email;
            private String date;
            
            public String getName() {
                return name;
            }
            
            public String getEmail() {
                return email;
            }
            
            public String getDate() {
                return date;
            }
        }
    }
}