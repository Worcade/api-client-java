// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.api;

import net.worcade.client.Result;
import net.worcade.client.api.mixin.LabelsApi;
import net.worcade.client.api.mixin.RemoteIdsApi;
import net.worcade.client.get.Attachment;
import net.worcade.client.get.AttachmentData;
import net.worcade.client.get.Reference;

import javax.ws.rs.core.MediaType;
import java.io.InputStream;

public interface AttachmentApi extends LabelsApi, RemoteIdsApi {
    Result<? extends Attachment> get(String id);
    Result<AttachmentData> getData(String id);
    Result<? extends Reference> create(String name, InputStream data, MediaType contentType);
    Result<?> delete(String id);
}
