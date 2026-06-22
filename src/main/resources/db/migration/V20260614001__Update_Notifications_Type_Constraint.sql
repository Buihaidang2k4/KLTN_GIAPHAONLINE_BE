-- Fix notifications type CHECK constraint to include all NotificationType enum values
-- Remove old constraint and create new one with all enum values

ALTER TABLE notifications DROP CONSTRAINT IF EXISTS notifications_type_check;

ALTER TABLE notifications
ADD CONSTRAINT notifications_type_check CHECK (type IN (
    'FAMILY_INVITATION',
    'FAMILY_MEMBER_ADDED_OR_REJECT',
    'FAMILY_MEMBER_REMOVED',
    'FAMILY_MEMBER_ROLE_CHANGED',
    'FAMILY_EVENT_UPCOMING',
    'FAMILY_EVENT_TODAY',
    'FEEDBACK_USER',
    'SUBSCRIPTION_EXPIRES',
    'SUBSCRIPTION_EXPIRED',
    'FEEDBACK_ADMIN'
));
