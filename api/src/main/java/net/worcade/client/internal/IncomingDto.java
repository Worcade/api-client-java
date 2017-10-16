// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.internal;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import net.worcade.client.exception.InvalidValueTypeException;
import net.worcade.client.get.ApplicationProfile;
import net.worcade.client.get.Asset;
import net.worcade.client.get.Attachment;
import net.worcade.client.get.Company;
import net.worcade.client.get.Contact;
import net.worcade.client.get.Conversation;
import net.worcade.client.get.ConversationContent;
import net.worcade.client.get.ExternalNumber;
import net.worcade.client.get.GroupProfile;
import net.worcade.client.get.Label;
import net.worcade.client.get.Notification;
import net.worcade.client.get.Reference;
import net.worcade.client.get.ReferenceWithName;
import net.worcade.client.get.ReferenceWithNumber;
import net.worcade.client.get.RemoteId;
import net.worcade.client.get.RemoteIdSearchResult;
import net.worcade.client.get.Room;
import net.worcade.client.get.Site;
import net.worcade.client.get.UserProfile;
import net.worcade.client.get.Webhook;
import net.worcade.client.get.WebhookTestResult;
import net.worcade.client.get.WorkOrder;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@ToString
class IncomingDto implements ApplicationProfile, Asset, Attachment, Company, Contact, Conversation, GroupProfile, Label,
        Room, Site, UserProfile, Webhook, WorkOrder,
        Site.Coordinates, RemoteId, RemoteIdSearchResult, ExternalNumber, ConversationContent, Webhook.Header, Webhook.Log,
        WebhookTestResult, ReferenceWithNumber, Notification, Notification.Tracking, WorkOrder.Row {
    static IncomingDto of(Map<String, Object> data) {
        return data == null ? null : new IncomingDto(data);
    }

    @Getter(AccessLevel.PACKAGE) private final ImmutableMap<String, Object> data;

    IncomingDto(Map<String, Object> data) {
        this.data = ImmutableMap.copyOf(Maps.filterValues(data, Objects::nonNull));
    }

    String getString(String key) {
        return get(key, String.class, true);
    }

    int getInt(String key) {
        return get(key, Integer.class, false);
    }

    Integer getInteger(String key) {
        return get(key, Integer.class, true);
    }

    Long getLong(String key) {
        return get(key, Long.class, true);
    }

    Instant getTimestamp(String key) {
        Integer timestamp = getInteger(key);
        return timestamp == null ? null : Instant.ofEpochSecond(timestamp);
    }

    Float getFloat(String key) {
        return get(key, Double.class, false).floatValue();
    }

    IncomingDto getDto(String key) {
        @SuppressWarnings("unchecked") Map<String, Object> data = get(key, Map.class, true);
        return of(data);
    }

    List<IncomingDto> getListOfDtos(String key) {
        @SuppressWarnings("unchecked") List<Map<String, Object>> data = get(key, List.class, true);
        return data == null ? ImmutableList.of() : ImmutableList.copyOf(Lists.transform(data, IncomingDto::of));
    }

    <T> List<T> getList(String key) {
        @SuppressWarnings("unchecked") List<T> data = get(key, List.class, true);
        return data == null ? ImmutableList.of() : ImmutableList.copyOf(data);
    }

    boolean getBoolean(String key) {
        return get(key, Boolean.class, false);
    }

    @Override
    public Modification modify() {
        return Modification.update(data);
    }

    @Override
    public String getId() {
        return getString("id");
    }

    @Override
    public String getType() {
        return getString("type");
    }

    @Override
    public boolean isDeleted() {
        return getBoolean("deleted");
    }

    @Override
    public Instant created() {
        return getTimestamp("created");
    }

    @Override
    public ReferenceWithName createdBy() {
        return getDto("creator");
    }

    @Override
    public Instant getModified() {
        return getTimestamp("modified");
    }

    @Override
    public ReferenceWithName modifier() {
        return getDto("modifier");
    }

    @Override
    public String getName() {
        return getString("name");
    }

    @Override
    public String getNumber() {
        return getString("number");
    }

    @Override
    public String getDescription() {
        return getString("description");
    }

    @Override
    public String getEmail() {
        return getString("email");
    }

    @Override
    public String getFloor() {
        return getString("floor");
    }

    @Override
    public String getRoomNumber() {
        return getString("roomNumber");
    }

    @Override
    public String getAddressLineOne() {
        return getString("addressLineOne");
    }

    @Override
    public String getAddressLineTwo() {
        return getString("addressLineTwo");
    }

    @Override
    public String getPostalCode() {
        return getString("postalCode");
    }

    @Override
    public String getCity() {
        return getString("city");
    }

    @Override
    public String getRegion() {
        return getString("region");
    }

    @Override
    public String getCountry() {
        return getString("country");
    }

    @Override
    public String getTelephone() {
        return getString("telephone");
    }

    @Override
    public String getAssetType() {
        return getString("assetType");
    }

    @Override
    public String getAssetMake() {
        return getString("assetMake");
    }

    @Override
    public String getAssetModel() {
        return getString("assetModel");
    }

    @Override
    public String getSerial() {
        return getString("serial");
    }

    @Override
    public String getSpecification() {
        return getString("specification");
    }

    @Override
    public String getRemoteIdType() {
        return getString("remoteIdType");
    }

    @Override
    public String getRemoteId() {
        return getString("remoteId");
    }

    @Override
    public Coordinates getCoordinates() {
        return getDto("coordinates");
    }

    @Override
    public float getLatitude() {
        return getFloat("latitude");
    }

    @Override
    public float getLongitude() {
        return getFloat("longitude");
    }

    @Override
    public Collection<? extends ReferenceWithName> getLabels() {
        return getListOfDtos("labels");
    }

    @Override
    public Collection<? extends ReferenceWithName> getOwners() {
        return getListOfDtos("owners");
    }

    @Override
    public Collection<? extends RemoteId> getRemoteIds() {
        return getListOfDtos("remoteIds");
    }

    @Override
    public boolean hasPassword() {
        return getBoolean("hasPassword");
    }

    @Override
    public Locale getLocale() {
        IncomingDto locale = getDto("locale");
        return locale == null ? null : new Locale(locale.getString("language"), locale.getString("country"));
    }

    @Override
    public ReferenceWithName getLocation() {
        return getDto("location");
    }

    @Override
    public ReferenceWithName getPicture() {
        return getDto("picture");
    }

    @Override
    public Collection<? extends ReferenceWithName> getProGroups() {
        return getListOfDtos("proGroups");
    }

    @Override
    public String getMailImportAddress() {
        return getString("mailImportAddress");
    }

    @Override
    public ReferenceWithName getAutoShareTarget() {
        return getDto("autoShareTarget");
    }

    @Override
    public boolean isSuppressUpdateNotifications() {
        return getBoolean("suppressUpdateNotifications");
    }

    @Override
    public boolean isSubscribedToGettingStarted() {
        return getBoolean("subscribeGettingStarted");
    }

    @Override
    public boolean isSubscribedToNewsletter() {
        return getBoolean("subscribeNewsletter");
    }

    @Override
    public Collection<? extends ReferenceWithName> getTrustedApplications() {
        return getListOfDtos("trustedApplications");
    }

    @Override
    public Collection<? extends ExternalNumber> getExternalNumbers() {
        return getListOfDtos("externalNumbers");
    }

    @Override
    public ReferenceWithName getAssignee() {
        return getDto("assignee");
    }

    @Override
    public ReferenceWithName getReporter() {
        return getDto("reporter");
    }

    @Override
    public Instant getLastContentTimestamp() {
        return getTimestamp("lastContent");
    }

    @Override
    public Collection<? extends ConversationContent> getConversationContent() {
        return getListOfDtos("content");
    }

    @Override
    public Reference getContent() {
        return getDto("content");
    }

    @Override
    public Instant getTimestamp() {
        return getTimestamp("timestamp");
    }

    @Override
    public ReferenceWithName getSource() {
        return getDto("source");
    }

    @Override
    public String getMessage() {
        return getString("message");
    }

    @Override
    public int getRating() {
        return getInt("rating");
    }

    @Override
    public boolean isPro() {
        return getBoolean("pro");
    }

    @Override
    public boolean isVisible() {
        return getBoolean("visible");
    }

    @Override
    public Collection<String> getDomains() {
        return getList("domains");
    }

    @Override
    public Collection<? extends ReferenceWithName> getMembers() {
        return getListOfDtos("members");
    }

    @Override
    public ReferenceWithName getCompany() {
        return getDto("company");
    }

    @Override
    public String getUrl() {
        return getString("url");
    }

    @Override
    public String getEvent() {
        return getString("event");
    }

    @Override
    public boolean isSuppressOwn() {
        return getBoolean("suppressOwn");
    }

    @Override
    public Collection<? extends Header> getHeaders() {
        return getListOfDtos("headers");
    }

    @Override
    public int getStatus() {
        return getInt("status");
    }

    @Override
    public String getResponseBody() {
        return getString("response");
    }

    @Override
    public String getValue() {
        return getString("value");
    }

    @Override
    public String getBackgroundColor() {
        return getString("backgroundColor");
    }

    @Override
    public String getForegroundColor() {
        return getString("foregroundColor");
    }

    @Override
    public Reference forGroup() {
        return getDto("forGroup");
    }

    @Override
    public String getMimeType() {
        return getString("mimeType");
    }

    @Override
    public int getSize() {
        return getInt("size");
    }

    @Override
    public Integer getHeight() {
        return getInteger("height");
    }

    @Override
    public Integer getWidth() {
        return getInteger("width");
    }

    @Override
    public Reference getTarget() {
        return getDto("target");
    }

    @Override
    public Reference getSubject() {
        return getDto("subject");
    }

    @Override
    public Collection<? extends Tracking> getTrackings() {
        return getListOfDtos("trackings");
    }

    @Override
    public Collection<? extends Row> getRows() {
        return getListOfDtos("rows");
    }

    @Override
    public boolean isApproved() {
        return Objects.equals(true, get("approved", Boolean.class, true));
    }

    @Override
    public boolean isRejected() {
        return Objects.equals(false, get("approved", Boolean.class, true));
    }

    @Override
    public Duration getDuration() {
        Long duration = getLong("duration");
        return duration == null ? null : Duration.of(duration, ChronoUnit.SECONDS);
    }

    @Override
    public Double getCostAmount() {
        return get("costAmount", Double.class, true);
    }

    @Override
    public Currency getCostCurrency() {
        return Currency.getInstance(getString("costCurrency"));
    }

    @Override
    public String getBody() {
        return getString("body");
    }

    private <T> T get(String key, Class<T> type, boolean allowNull) {
        Object value = data.get(key);
        if (type.isInstance(value)) {
            @SuppressWarnings("unchecked") T typedValue = (T) value;
            return typedValue;
        }
        else if (allowNull && value == null) {
            return null;
        }
        throw new InvalidValueTypeException(type, key, value);
    }
}
