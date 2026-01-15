package com.example.myapp;

/**
 * Utility class to map backend avatar IDs to Android drawable resources
 * Backend sends simple integers (1, 2, 3, etc.)
 * This class converts them to actual drawable resource IDs
 */
public class AvatarMapper {

    /**
     * Maps tutor avatar IDs from backend to drawable resources
     * @param backendAvatarId The integer ID from backend (1, 2, 3, etc.)
     * @return The corresponding drawable resource ID
     */
    public static int getTutorAvatarResource(int backendAvatarId) {
        switch (backendAvatarId) {
            case 1:
                return R.drawable.avatar_tutor_emily;
            case 2:
                return R.drawable.avatar_tutor_marcus;
            // Add more cases as you add more tutor avatars
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                // For now, alternate between available avatars
                return (backendAvatarId % 2 == 0) ? R.drawable.avatar_tutor_marcus : R.drawable.avatar_tutor_emily;
            default:
                // Default avatar for unknown IDs
                return R.drawable.ic_tutor_avatar;
        }
    }

    /**
     * Maps student avatar IDs from backend to drawable resources
     * @param backendAvatarId The integer ID from backend (1, 2, 3, etc.)
     * @return The corresponding drawable resource ID
     */
    public static int getStudentAvatarResource(int backendAvatarId) {
        switch (backendAvatarId) {
            case 1:
                return R.drawable.avatar_student_jessica;
            case 2:
                return R.drawable.avatar_student_michael;
            // Add more cases as you add more student avatars
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                // For now, alternate between available avatars
                return (backendAvatarId % 2 == 0) ? R.drawable.avatar_student_michael : R.drawable.avatar_student_jessica;
            default:
                // Default avatar
                return R.drawable.avatar_student_jessica;
        }
    }
}

