package fan.summer.hmoneta.official.dns.plugin.tencent;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.dnspod.v20210323.DnspodClient;
import com.tencentcloudapi.dnspod.v20210323.models.*;
import fan.summer.hmoneta.plugin.api.dns.HmDnsProviderPlugin;
import fan.summer.hmoneta.plugin.api.dns.dto.DNSRecordInfo;
import org.pf4j.Extension;
import org.pf4j.PluginWrapper;
import org.pf4j.spring.SpringPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.*;
import java.util.stream.Collectors;

public class TencentPlugin extends SpringPlugin {

    public TencentPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }
    @Override
    protected ApplicationContext createApplicationContext() {

        return null;
    }

    @Extension(ordinal=1)
    public static class TencentCloud implements HmDnsProviderPlugin {
        private static final Logger log = LoggerFactory.getLogger(TencentCloud.class);
        private DnspodClient client;

        @Override
        public void authenticate(Map<String, String> credentials) {
            client = new DnspodClient(
                    new Credential(credentials.get("accessKeyId"), credentials.get("accessKeySecret")),
                    ""
            );
        }

        @Override
        public Set<String> authenticateWay() {
            return Set.of("accessKeyId", "accessKeySecret");
        }

        @Override
        public String providerName() {
            return "腾讯云";
        }

        @Override
        public List<DNSRecordInfo> dnsCheck(String domain, String subDomain) {
            try {
                HttpProfile httpProfile = new HttpProfile();
                httpProfile.setEndpoint("dnspod.tencentcloudapi.com");
                ClientProfile clientProfile = new ClientProfile();
                clientProfile.setHttpProfile(httpProfile);

                DescribeRecordListRequest req = new DescribeRecordListRequest();
                req.setDomain(domain);
                DescribeRecordListResponse resp = client.DescribeRecordList(req);

                return Arrays.stream(resp.getRecordList())
                        .filter(r -> r.getName().equals(subDomain))
                        .map(r -> new DNSRecordInfo(r.getType(), r.getRecordId(), r.getValue()))
                        .collect(Collectors.toList());

            } catch (TencentCloudSDKException e) {
                log.error("DNS 查询失败", e);
                return Collections.emptyList();
            }
        }

        @Override
        public boolean modifyDns(String domain, String subDomain, String type, String value) {
            try {
                List<DNSRecordInfo> records = dnsCheck(domain, subDomain);
                Optional<DNSRecordInfo> existing = records.stream()
                        .filter(r -> r.gettype().equals(type))
                        .findFirst();

                if (existing.isEmpty()) {
                    // 创建新记录
                    CreateRecordRequest req = new CreateRecordRequest();
                    req.setDomain(domain);
                    req.setSubDomain(subDomain);
                    req.setRecordType(type);
                    req.setRecordLine("默认");
                    req.setValue(value);
                    client.CreateRecord(req);
                } else if (!existing.get().getvalue().equals(value)) {
                    // 修改记录
                    ModifyRecordRequest req = new ModifyRecordRequest();
                    req.setDomain(domain);
                    req.setRecordId(existing.get().getrecordId());
                    req.setSubDomain(subDomain);
                    req.setRecordType(type);
                    req.setRecordLine("默认");
                    req.setValue(value);
                    client.ModifyRecord(req);
                }
                return true;
            } catch (TencentCloudSDKException e) {
                log.error("DNS 修改失败", e);
                return false;
            }
        }

        @Override
        public boolean deleteDns(String domain, String subDomain, String type) {
            try {
                List<DNSRecordInfo> records = dnsCheck(domain, subDomain);
                return records.stream()
                        .filter(r -> r.gettype().equals(type))
                        .allMatch(r -> {
                            try {
                                DeleteRecordRequest req = new DeleteRecordRequest();
                                req.setDomain(domain);
                                req.setRecordId(r.getrecordId());
                                client.DeleteRecord(req);
                                return true;
                            } catch (TencentCloudSDKException e) {
                                log.error("删除失败", e);
                                return false;
                            }
                        });
            } catch (Exception e) {
                log.error("删除过程出错", e);
                return false;
            }
        }

    }



}
