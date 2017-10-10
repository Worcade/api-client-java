// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.internal;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import net.worcade.client.create.ApplicationCreate;
import net.worcade.client.create.AssetCreate;
import net.worcade.client.create.CompanyCreate;
import net.worcade.client.create.ConversationCreate;
import net.worcade.client.create.GroupCreate;
import net.worcade.client.create.LabelCreate;
import net.worcade.client.create.RoomCreate;
import net.worcade.client.create.SiteCreate;
import net.worcade.client.create.UserCreate;
import net.worcade.client.create.WebhookCreate;
import net.worcade.client.create.WorkOrderCreate;
import net.worcade.client.get.ExternalNumber;
import net.worcade.client.get.Reference;
import net.worcade.client.get.RemoteId;
import net.worcade.client.get.Webhook;
import net.worcade.client.get.WorkOrder;
import net.worcade.client.modify.WorkOrderModification;
import net.worcade.client.modify.WorkOrderRowModification;

import java.time.Duration;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

@ToString
class Modification implements ApplicationCreate, AssetCreate, CompanyCreate, ConversationCreate, GroupCreate,
        LabelCreate, RoomCreate, SiteCreate, UserCreate, WebhookCreate, WorkOrderCreate, WorkOrderModification, WorkOrderRowModification {
    static Modification update(Map<String, Object> data) {
        return new Modification(data);
    }

    static Modification create() {
        return new Modification();
    }

    @Getter(AccessLevel.PACKAGE) private final Map<String, Object> data;

    private Modification() {
        this.data = Maps.newHashMap();
    }

    private Modification(Map<String, Object> data) {
        this.data = Maps.newHashMap(data);
    }

    private Modification putReference(String key, Reference reference) {
        return put(key, cleanReference(reference));
    }

    private Modification putReferenceList(String key, Reference... references) {
        return put(key, cleanReferences(references));
    }

    private Modification put(String key, Object value) {
        data.put(key, value);
        return this;
    }

    static Map<String, String> cleanReference(String id) {
        return ImmutableMap.of("id", WorcadeClient.checkId(id));
    }

    static Map<String, String> cleanReference(Reference reference) {
        return ImmutableMap.of("id", WorcadeClient.checkId(reference.getId()));
    }

    static List<Map<String, String>> cleanReferences(Reference... references) {
        return Stream.of(references).map(Modification::cleanReference).collect(ImmutableList.toImmutableList());
    }

    static List<Map<String, String>> cleanRemoteIds(RemoteId... remoteIds) {
        return Stream.of(remoteIds)
                .map(r -> ImmutableMap.of("remoteIdType", r.getRemoteIdType(), "remoteId", r.getRemoteId()))
                .collect(ImmutableList.toImmutableList());
    }

    static List<Map<String, String>> cleanExternalNumbers(ExternalNumber... numbers) {
        return Stream.of(numbers)
                .map(r -> ImmutableMap.of("number", r.getNumber(), "description", r.getDescription()))
                .collect(ImmutableList.toImmutableList());
    }

    static List<Map<String, Object>> cleanHeaders(Webhook.Header... headers) {
        return Stream.of(headers)
                .map(r -> ((IncomingDto) r).getData())
                .collect(ImmutableList.toImmutableList());
    }

    static List<ImmutableMap<String, Object>> cleanRows(WorkOrder.Row... rows) {
        return Stream.of(rows)
                .map(r -> ((IncomingDto) r).getData())
                .collect(ImmutableList.toImmutableList());
    }

    @Override
    public Modification name(String name) {
        return put("name", name);
    }

    @Override
    public Modification email(String name) {
        return put("email", name);
    }

    @Override
    public Modification password(String password) {
        return put("password", Util.encodeBase64(password));
    }

    @Override
    public Modification type(String type) {
        return put("assetType", type);
    }

    @Override
    public Modification make(String make) {
        return put("assetMake", make);
    }

    @Override
    public Modification model(String model) {
        return put("assetModel", model);
    }

    @Override
    public Modification specification(String specification) {
        return put("specification", specification);
    }

    @Override
    public Modification serial(String serial) {
        return put("serial", serial);
    }

    @Override
    public Modification addressLineOne(String addressLineOne) {
        return put("addressLineOne", addressLineOne);
    }

    @Override
    public Modification addressLineTwo(String addressLineTwo) {
        return put("addressLineTwo", addressLineTwo);
    }

    @Override
    public Modification postalCode(String postalCode) {
        return put("postalCode", postalCode);
    }

    @Override
    public Modification city(String city) {
        return put("city", city);
    }

    @Override
    public Modification region(String region) {
        return put("region", region);
    }

    @Override
    public Modification country(String country) {
        return put("country", country);
    }

    @Override
    public Modification telephone(String telephone) {
        return put("telephone", telephone);
    }

    @Override
    public Modification coordinates(float latitude, float longitude) {
        return put("coordinates", ImmutableMap.of("latitude", latitude, "longitude", longitude));
    }

    @Override
    public Modification versions(String... versions) {
        return put("versions", ImmutableList.copyOf(versions));
    }

    @Override
    public Modification locale(Locale locale) {
        return put("locale", ImmutableMap.of("language", locale.getLanguage(), "country", locale.getCountry()));
    }

    @Override
    public Modification mailImportAddress(String mailImportAddress) {
        return put("mailImportAddress", mailImportAddress);
    }

    @Override
    public Modification location(Reference location) {
        return putReference("location", location);
    }

    @Override
    public Modification autoShareTarget(Reference target) {
        return putReference("autoShareTarget", target);
    }

    @Override
    public Modification picture(Reference picture) {
        return putReference("picture", picture);
    }

    @Override
    public Modification suppressUpdateNotifications(boolean suppress) {
        return put("suppressUpdateNotifications", suppress);
    }

    @Override
    public Modification subscribeGettingStarted(boolean subscribe) {
        return put("subscribeGettingStarted", subscribe);
    }

    @Override
    public Modification subscribeNewsletter(boolean subscribe) {
        return put("subscribeNewsletter", subscribe);
    }

    @Override
    public Modification trustedApplications(Reference... applications) {
        return putReferenceList("trustedApplications", applications);
    }

    @Override
    public Modification remoteIds(RemoteId... remoteIds) {
        return put("remoteIds", cleanRemoteIds(remoteIds));
    }

    @Override
    public Modification domains(String... domains) {
        return put("domains", ImmutableList.copyOf(domains));
    }

    @Override
    public Modification members(Reference... members) {
        return putReferenceList("members", members);
    }

    @Override
    public Modification company(Reference company) {
        return putReference("company", company);
    }

    @Override
    public Modification visible(boolean visible) {
        return put("visible", visible);
    }

    @Override
    public Modification reporter(Reference reporter) {
        return putReference("reporter", reporter);
    }

    @Override
    public Modification assignee(Reference assignee) {
        return putReference("assignee", assignee);
    }

    @Override
    public Modification testMode(boolean testMode) {
        return put("testMode", testMode);
    }

    @Override
    public Modification externalNumbers(ExternalNumber... numbers) {
        return put("externalNumbers", cleanExternalNumbers(numbers));
    }

    @Override
    public Modification watchers(Reference... watchers) {
        return putReferenceList("watchers", watchers);
    }

    @Override
    public Modification close() {
        return put("closed", true);
    }

    @Override
    public Modification reopen() {
        return put("closed", false);
    }

    @Override
    public Modification archive() {
        return put("archived", true);
    }

    @Override
    public Modification unarchive() {
        return put("archived", false);
    }

    @Override
    public Modification floor(String floor) {
        return put("floor", floor);
    }

    @Override
    public Modification roomNumber(String roomNumber) {
        return put("roomNumber", roomNumber);
    }

    @Override
    public Modification labels(Reference... labels) {
        return putReferenceList("labels", labels);
    }

    @Override
    public Modification sharedWith(Reference... targets) {
        return putReferenceList("sharedWith", targets);
    }

    @Override
    public Modification url(String url) {
        return put("url", url);
    }

    @Override
    public Modification event(Webhook.Event event) {
        return put("event", event.getEvent());
    }

    @Override
    public Modification suppressOwn(boolean suppress) {
        return put("suppressOwn", suppress);
    }

    @Override
    public Modification headers(Webhook.Header... headers) {
        return put("headers", cleanHeaders(headers));
    }

    @Override
    public Modification description(String description) {
        return put("description", description);
    }

    @Override
    public Modification backgroundColor(String color) {
        return put("backgroundColor", color);
    }

    @Override
    public Modification foregroundColor(String color) {
        return put("foregroundColor", color);
    }

    @Override
    public Modification forGroup(Reference group) {
        return putReference("forGroup", group);
    }

    @Override
    public Modification rows(WorkOrder.Row... rows) {
        return put("rows", cleanRows(rows));
    }

    @Override
    public Modification approve() {
        return put("approved", true);
    }

    @Override
    public Modification reject() {
        return put("approved", false);
    }

    @Override
    public WorkOrderRowModification duration(Duration duration) {
        return put("duration", duration.getSeconds());
    }

    @Override
    public WorkOrderRowModification cost(double amount, Currency currency) {
        return put("costAmount", amount).put("costCurrency", currency.getCurrencyCode());
    }
}
